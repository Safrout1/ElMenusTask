
// @GENERATOR:play-routes-compiler
// @SOURCE:/Users/safrout/Desktop/play-scala-rest-api-example/conf/routes
// @DATE:Sat Nov 18 23:18:59 EET 2017


package router {
  object RoutesPrefix {
    private var _prefix: String = "/"
    def setPrefix(p: String): Unit = {
      _prefix = p
    }
    def prefix: String = _prefix
    val byNamePrefix: Function0[String] = { () => prefix }
  }
}
