import kotlinx.coroutines.*
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlin.system.measureTimeMillis

var carCouterMutex = Mutex()
var totalTimeMutex = Mutex()
var readyCars = 0
var totalTimeTaken: Long = 0

fun main(args: Array<String>) = runBlocking {

        val carsToProduce = args[0].toIntOrNull()
        if(carsToProduce == null) { return@runBlocking }

        repeat(carsToProduce){
                launch {
                    val milliseconds = measureTimeMillis {
                        buildCar()
                        carCouterMutex.withLock { readyCars += 1 }
                    }
                    totalTimeMutex.withLock { totalTimeTaken += milliseconds }
                }
        }

    delay(60000L)

    val totalTime            = totalTimeTaken.toMinutesAndSecond()
    val avgCarBuildTime = (totalTimeTaken / readyCars).toMinutesAndSecond()

    println("Time Taken to Build Single Car is ${avgCarBuildTime.minutes} minutes ${avgCarBuildTime.seconds} seconds")
    println("Total time taken to build $readyCars is ${totalTime.minutes} minutes ${totalTime.seconds} seconds")

}

suspend fun buildCar(){
    //Build Body
    delay(5000L)
    println("Body Making Completed")

    //Paint car
    delay(5000L)
    println("Painting Completed")

    //Install driver traint
    delay(10000L)
    println("Driver traint Installed")

    //Install Batteries
    delay(15000L)
    println("Battery Installed")

    //install Interior
    delay(15000L)
    println("Interior Installed")

}

data class Time(val minutes: Long, val seconds: Long)
fun Long.toMinutesAndSecond(): Time {
    val minutes = this / 1000 / 60
    val seconds = this / 1000 % 60
    return Time(minutes, seconds)
}
