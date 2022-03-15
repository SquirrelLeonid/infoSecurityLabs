using System;

namespace lab_2
{
    internal class Program
    {
        private static void Main()
        {
            var input = GetFileName("Enter file with secret word");
            var documentName = GetFileName("Enter document name");

            var fileWorker = new FileWorker();

            var secretWord = fileWorker.GetTxtFileContent(input);
            var documentContent = fileWorker.GetDocumentContent(documentName);

            var steganograph = new Steganograph(documentName, documentContent, secretWord);

            if (steganograph.IsSteganographyPossible())
            {
                steganograph.WriteSecretWordToDocument();
                Console.WriteLine("Done!");
            }
            else
            {
                Console.WriteLine($"Unable to hide secret in {documentName}");
            }
        }

        private static string GetFileName(string promtMessage)
        {
            Console.WriteLine(promtMessage);
            return Console.ReadLine();
        }
    }
}