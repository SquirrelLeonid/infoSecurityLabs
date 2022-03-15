using System.Text;
using DocumentFormat.OpenXml;
using DocumentFormat.OpenXml.Packaging;
using DocumentFormat.OpenXml.Wordprocessing;

namespace lab_2
{
    internal class Steganograph
    {
        private readonly string documentContent;
        private readonly string documentName;
        private readonly string secretWord;

        private RunProperties regularProperties;

        public Steganograph(string documentName, string documentContent, string secretWord)
        {
            this.documentName = documentName;
            this.documentContent = documentContent;
            this.secretWord = secretWord;
        }

        public bool IsSteganographyPossible()
        {
            if (string.IsNullOrWhiteSpace(secretWord) || string.IsNullOrEmpty(documentContent))
                return false;

            if (secretWord.Length > documentContent.Length)
                return false;

            return IsAbleToFitSecretInDocument();
        }

        public void WriteSecretWordToDocument()
        {
            var regularText = new StringBuilder();

            using var documentFile = WordprocessingDocument.Open(documentName, true);
            {
                SetRegularFontAndSize(documentFile);
                documentFile.DeletePart(documentFile.MainDocumentPart);

                var mainPart = documentFile.AddMainDocumentPart();
                var paragraph = new Paragraph();
                mainPart.Document = new Document();

                var currentSecretCharIndex = 0;
                var currentSecretChar = secretWord[currentSecretCharIndex];
                var allSecretCharsAdded = false;

                for (var i = 0; i < documentContent.Length; i++)
                {
                    if (!allSecretCharsAdded && documentContent[i] == currentSecretChar)
                    {
                        paragraph.AppendChild(CreateRegularTextRun(regularText));
                        paragraph.AppendChild(CreateSecretTextRun(documentContent[i]));
                        regularText.Clear();

                        currentSecretCharIndex++;
                        if (currentSecretCharIndex >= secretWord.Length)
                            allSecretCharsAdded = true;
                        else
                            currentSecretChar = secretWord[currentSecretCharIndex];
                        continue;
                    }

                    regularText.Append(documentContent[i]);
                }

                paragraph.AppendChild(CreateRegularTextRun(regularText));
                mainPart.Document.Append(new Body(paragraph));
                mainPart.Document.Save();
                documentFile.Close();
            }
        }

        private bool IsAbleToFitSecretInDocument()
        {
            var secretChars = secretWord.ToCharArray();

            return documentContent.ContainsAllChars(secretChars)
                   && documentContent.ContainsCharsInLeftToRightOrder(secretChars);
        }

        private void SetRegularFontAndSize(WordprocessingDocument document)
        {
            var body = document.MainDocumentPart?.Document.Body;
            var firstParagraph = body?.ChildElements.First<Paragraph>();
            var firstRun = firstParagraph?.ChildElements.First<Run>();
            regularProperties = firstRun?.RunProperties;
        }

        private Run CreateRegularTextRun(StringBuilder regularText)
        {
            var run = new Run();
            run.AppendChild(new Text
            {
                Text = regularText.ToString(),
                Space = SpaceProcessingModeValues.Preserve
            });

            var runProperties = new RunProperties();
            var font = new RunFonts
            {
                Ascii = regularProperties?.RunFonts?.Ascii,
                HighAnsi = regularProperties?.RunFonts?.Ascii
            };
            var size = new FontSize { Val = regularProperties?.FontSize?.Val };

            runProperties.AppendChild(font);
            runProperties.AppendChild(size);
            run.PrependChild(runProperties);

            return run;
        }

        private Run CreateSecretTextRun(char secretChar)
        {
            var run = new Run();

            run.AppendChild(new Text
            {
                Text = "" + secretChar,
                Space = SpaceProcessingModeValues.Preserve
            });

            var runProperties = new RunProperties();
            var font = new RunFonts
            {
                Ascii = regularProperties?.RunFonts?.Ascii,
                HighAnsi = regularProperties?.RunFonts?.Ascii
            };
            var size = new FontSize { Val = GetSecretCharSize() };

            runProperties.AppendChild(font);
            runProperties.AppendChild(size);
            run.PrependChild(runProperties);

            return run;
        }

        private StringValue GetSecretCharSize()
        {
            var regularSize = int.Parse(regularProperties?.FontSize?.Val?.Value);
            return new StringValue("" + (regularSize - 2));
        }
    }
}