import sbt._
import uk.gov.hmrc.SbtAutoBuildPlugin
import uk.gov.hmrc.sbtdistributables.SbtDistributablesPlugin
import uk.gov.hmrc.versioning.SbtGitVersioning
import play.sbt.PlayImport._

object MicroServiceBuild extends Build with MicroService {
  import play.sbt.routes.RoutesKeys._

  val appName = "push-registration"

  override lazy val plugins: Seq[Plugins] = Seq(
    SbtAutoBuildPlugin, SbtGitVersioning, SbtDistributablesPlugin
  )

  override lazy val appDependencies: Seq[ModuleID] = AppDependencies()
  override lazy val playSettings : Seq[Setting[_]] = Seq(routesImport ++= Seq("uk.gov.hmrc.pushregistration.binders.Binders._"))
}

private object AppDependencies {
  import play.sbt.PlayImport._
  import play.core.PlayVersion

  private val microserviceBootstrapVersion = "5.6.0"
  private val playAuthVersion = "4.2.0"
  private val playHealthVersion = "2.0.0"
  private val playJsonLoggerVersion = "3.0.0"
  private val playUrlBindersVersion = "2.0.0"
  private val playConfigVersion = "3.0.0"
  private val domainVersion = "4.0.0"
  private val playHmrcApiVersion = "1.1.0"
  private val hmrcTestVersion = "2.0.0"
  private val pegdownVersion = "1.6.0"
  private val scalaTestVersion = "2.2.6"
  private val wireMockVersion = "2.2.2"
  private val cucumberVersion = "1.2.5"
  private val scalaJVersion = "1.1.6"
  private val reactiveMongoTest = "1.6.0"
  private val playReactiveMongo = "5.1.0"
  private val playUI: String = "5.1.0"
  private val circuitBreaker = "1.7.0"

  val compile = Seq(
    ws,
    "uk.gov.hmrc" %% "microservice-bootstrap" % microserviceBootstrapVersion,
    "uk.gov.hmrc" %% "play-authorisation" % playAuthVersion,
    "uk.gov.hmrc" %% "play-health" % playHealthVersion,
    "uk.gov.hmrc" %% "play-url-binders" % playUrlBindersVersion,
    "uk.gov.hmrc" %% "play-config" % playConfigVersion,
    "uk.gov.hmrc" %% "play-json-logger" % playJsonLoggerVersion,
    "uk.gov.hmrc" %% "domain" % domainVersion,
    "uk.gov.hmrc" %% "reactive-circuit-breaker" % circuitBreaker,
    "uk.gov.hmrc" %% "play-ui" %  playUI,
    "uk.gov.hmrc" %% "play-hmrc-api" % playHmrcApiVersion,
    "uk.gov.hmrc" %% "play-reactivemongo" % playReactiveMongo
  )

  trait TestDependencies {
    lazy val scope: String = "test"
    lazy val test : Seq[ModuleID] = ???
  }

  object Test {
    def apply() = new TestDependencies {
      override lazy val test = Seq(
        "uk.gov.hmrc" %% "hmrctest" % hmrcTestVersion % "test,it",
        "org.scalaj" %% "scalaj-http" % scalaJVersion % "test,it",
        "org.scalatest" %% "scalatest" % scalaTestVersion % "test,it",
        "org.pegdown" % "pegdown" % pegdownVersion % "test,it",
        "com.typesafe.play" %% "play-test" % PlayVersion.current % "test,it",
        "com.github.tomakehurst" % "wiremock" % wireMockVersion % "test,it",
        "info.cukes" %% "cucumber-scala" % cucumberVersion % "test,it",
        "info.cukes" % "cucumber-junit" % cucumberVersion % "test,it",
        "uk.gov.hmrc" %% "reactivemongo-test" % reactiveMongoTest % scope
      )
    }.test
  }

  object IntegrationTest {
    def apply() = new TestDependencies {

      override lazy val scope: String = "it"

      override lazy val test = Seq(
        "com.typesafe.play" %% "play-test" % PlayVersion.current % scope,
        "uk.gov.hmrc" %% "hmrctest" % hmrcTestVersion % scope,
        "org.pegdown" % "pegdown" % pegdownVersion % scope,
        "com.github.tomakehurst" % "wiremock" % wireMockVersion % scope,
        "org.mockito" % "mockito-all" % "1.9.5" % scope,
        "org.scalatestplus.play" %% "scalatestplus-play" % "1.5.1" % scope
      )
    }.test
  }

  def apply() = compile ++ Test() ++ IntegrationTest()
}
