using System.Collections.Generic;
using System.Linq;

namespace lab_2
{
    public static class StringExtensions
    {
        public static bool ContainsAllChars(this string str, char[] chars)
        {
            foreach (var ch in chars)
                if (!str.Contains(ch))
                    return false;

            return true;
        }

        public static bool ContainsCharsInLeftToRightOrder(this string str, char[] chars)
        {
            var found = new HashSet<char>();

            var i = 0;

            for (var currentChar = 0; currentChar < chars.Length; currentChar++)
            for (; i < str.Length; i++)
                if (chars[currentChar] == str[i])
                {
                    found.Add(chars[currentChar]);
                    break;
                }


            return found.Count != chars.Length;
        }
    }
}