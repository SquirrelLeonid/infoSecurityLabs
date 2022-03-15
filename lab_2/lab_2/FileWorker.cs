using System;
using System.IO;
using System.Linq;
using System.Text;
using DocumentFormat.OpenXml.Packaging;
using DocumentFormat.OpenXml.Wordprocessing;

namespace lab_2
{
    internal class FileWorker
    {
        public string GetTxtFileContent(string fileName)
        {
            using var reader = new StreamReader(fileName);
            {
                return reader.ReadToEnd();
            }
        }

        public string GetDocumentContent(string fileName)
        {
            var documentText = new StringBuilder();

            try
            {
                using var document = WordprocessingDocument.Open(fileName, false);
                var body = document.MainDocumentPart?.Document.Body;
                var paragraphs = body?.Elements<Paragraph>().ToList();

                paragraphs?.ForEach(paragraph => paragraph
                    .Elements<Run>()
                    .ToList()
                    .ForEach(run => documentText.Append(run.InnerText)));
            }

            catch (FileNotFoundException e)
            {
                throw new FileNotFoundException(e.Message);
            }
            catch (Exception e)
            {
                throw new Exception(e.Message);
            }

            return documentText.ToString();
        }
    }
}