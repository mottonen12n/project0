package pokedex

import scala.io.Source
import java.io.{FileNotFoundException, IOException}
import scala.io.BufferedSource

object Pokedex  {
    def main(args: Array[String]){

        val fileName = "C:/Users/Work/Project0/pokedex.csv"
        var bufferedSource:BufferedSource = null

        try{
            bufferedSource = Source.fromFile(fileName)
            for (line <- bufferedSource.getLines) {
                val cols = line.split(",").map(_.trim)
                //Do processing of input here
                println(s"${cols(0)}|${cols(1)}|${cols(2)}|${cols(3)}|${cols(4)}|${cols(5)}|${cols(6)}")
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

        var loop = true
        do {
            var selection = -1
            var nameToSearch = ""
            var numberToSearch = 0
            var typeToSearch = ""
            var pokemonToCatch = ""
            loop = true

            println("Welcome to the Pokedex! Please choose an operation:\n" +
              "(1) Search by Name\n" +
              "(2) Search by Number\n" +
              "(3) Search by Type\n" +
              "(4) Set a Pokemon to Caught\n" +
              "(0) Exit Pokedex")
            try {
                selection = readInt()
                selection match {
                case 1 => {
                    println("Enter Pokemon name to search: ")
                    nameToSearch = readLine().trim()
                    println(nameToSearch) //Processing goes here
                }
                case 2 => {
                    println("Enter Pokemon number to search: ")
                    numberToSearch = readInt()
                    println(numberToSearch) //Processing goes here
                }
                case 3 => {
                    println("Enter Pokemon type to search: ")
                    typeToSearch = readLine().trim()
                    if(checkType(typeToSearch)){
                        println(typeToSearch) //Processing goes here
                    } else println(s"Invalid type entered: $typeToSearch")
                }
                case 4 => {
                    println("Enter the Pokemon that you caught: ")
                    pokemonToCatch = readLine().trim()
                    println(pokemonToCatch) //Processing goes here
                }
                case 0 => {
                    println("Thank you for using the Pokedex!")
                    Thread.sleep(1000)
                    loop = false
                }
                case _ => {
                    println("Invalid selection, please try again.")
                }
            }
            }
            catch {
                case e: Exception => println("Invalid selection, please try again.")
            }
            
            }
        while(loop)

        def checkType(givenType: String) : Boolean = {
            givenType.trim.toUpperCase match {
                case "NORMAL" | "FIRE" | "WATER" | "GRASS" | "ELECTRIC" | "ICE" | "FIGHTING" | "POISON" | "GROUND" | "FLYING" | "PSYCHIC" | "BUG" | "ROCK" | "GHOST" | "DARK" | "DRAGON" | "STEEL" | "FAIRY" => return true
                case _ => return false
            }
        }

    }
}