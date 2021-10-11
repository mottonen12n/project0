package pokedex

import scala.io.Source
import java.io.{FileNotFoundException, IOException}
import scala.io.BufferedSource

object Pokedex  {
    def main(args: Array[String]){

        val fileName = "C:/Users/Work/Project0/pokedex2.csv"
        var bufferedSource:BufferedSource = null

        try{
            bufferedSource = Source.fromFile(fileName)
            for (line <- bufferedSource.getLines) {
                val cols = line.split(",").map(_.trim)
                //Do processing of input here
                println(s"${cols(0)}|${cols(1)}|${cols(2)}|${cols(3)}|${cols(4)}|${cols(5)}")
                //println(cols(0))
                //println(cols(1))
                //println(cols(2))
                //println(cols(3))
                //println(cols(4))
                //println(cols(5))
            }

        }
        catch {
            case e: FileNotFoundException => println(s"Did not find file with name: $fileName")
            case e: IOException => println("Got an IOException!")
        }
        finally {
            try {
            bufferedSource.close
            }
            catch {
                case e: NullPointerException => None
            }
        }
    }
}