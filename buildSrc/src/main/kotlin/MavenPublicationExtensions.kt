import org.gradle.api.publish.maven.MavenPublication
import org.gradle.kotlin.dsl.assign

fun MavenPublication.configureTwirpKmpPOM(pomDescription: String) {
    val pomName = artifactId

    pom {
        name = pomName
        description = pomDescription

        licenses {
            license {
                name = "MIT"
                url = "https://opensource.org/licenses/MIT"
            }
        }

        url = "https://github.com/collectiveidea/twirp-kmp"

        issueManagement {
            system = "Github"
            url = "https://github.com/collectiveidea/twirp-kmp/issues"
        }

        scm {
            connection = "https://github.com/collectiveidea/twirp-kmp.git"
            url = "https://github.com/collectiveidea/twirp-kmp"
        }

        developers {
            developer {
                id = "collectiveidea"
                name = "Collective Idea"
                url = "https://github.com/collectiveidea"
            }
        }
    }
}
