package charlie.laplacian.plugin

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class Plugin(val name: String,
                        val descriptor: String = "",

                        val version: String,
                        val versionID: String,

                        val author: String = "")