package covid.plugin

object PluginApp {
    @JvmStatic
    fun main(args: Array<String>) {
        println("plugin")
    }
}

//fun start() {
//    Window(title = "Compose for Desktop", size = IntSize(300, 300)) {
//        val count = remember { mutableStateOf(0) }
//        MaterialTheme {
//            Column(Modifier.fillMaxSize(), Arrangement.spacedBy(5.dp)) {
//                Button(modifier = Modifier.align(Alignment.CenterHorizontally),
//                    onClick = {
//                        count.value++
//                    }) {
//                    Text(if (count.value == 0) "Hello World" else "Clicked ${count.value}!")
//                }
//                Button(modifier = Modifier.align(Alignment.CenterHorizontally),
//                    onClick = {
//                        count.value = 0
//                    }) {
//                    Text("Reset")
//                }
//            }
//        }
//    }
//}