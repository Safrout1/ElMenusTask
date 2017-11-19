object ElMenusTask extends App {

  def factorial(n: Int): Long = return if (n <= 0) 1 else factorial(n - 1) * n

  def palindrome(s: String): Boolean = return if (s.length == 0 || s.length == 1) true else if (s.charAt(0) != s.charAt(s.length - 1)) false else palindrome(s.substring(1, s.length - 1));

  def runLengthEncode(s: String): String = {
    def getLength(c: Char, i: Int): Int = return if (i >= s.length || s.charAt(i) != c) 0 else return getLength(c, i + 1) + 1;
    val firstChar: Char = if (s.length == 0) '$' else s.charAt(0); // The $ here is a dummy character.
    val repetitions: Int = if (s.length == 0) 0 else getLength(firstChar, 0)
    return if (s.length == 0) "" else "" + firstChar + repetitions + runLengthEncode(s.substring(repetitions));
  }

  def runLengthDecode(s: String): String = {
    def getRepeatedChar(c: Char, n: Int): String = return if (n == 0) "" else "" + c + getRepeatedChar(c, n - 1);
    def getLengthOfRepetitions(i: Int): Int = return if (i >= s.length || s.charAt(i) < '0' || s.charAt(i) > '9') 0 else getLengthOfRepetitions(i + 1) + 1
    val lengthOfRepetitions: Int = getLengthOfRepetitions(1)
    return if (s.length == 0) "" else getRepeatedChar(s.charAt(0), s.substring(1, lengthOfRepetitions + 1).toInt) + runLengthDecode(s.substring(lengthOfRepetitions + 1))
  }

  def compose[A](f: A => A, g: A => A) = (in: A) => {f(g(in))}

  val inc = (n: Int) => n + 1;

  val square = (n: Int) => n * n;

  def h = compose(square, inc)


  println(factorial(5))

  println(palindrome("amanaplanacanalpanama"))
  println(palindrome("apple"))

  println(runLengthEncode("aaaaaaaaaabbbaxxxxyyyzyx"))

  println(runLengthDecode("a10b3a1x4y3z1y1x1"))

  println(h(6))
}
