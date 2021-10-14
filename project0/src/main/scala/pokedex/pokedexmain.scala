package pokedex

import scala.io.StdIn._
import scala.io.Source
import java.io.{FileNotFoundException, IOException}
import scala.io.BufferedSource
import org.mongodb.scala._
 import org.mongodb.scala.model._
 import org.mongodb.scala.model.Filters._
 import org.mongodb.scala.model.Updates._
 import org.mongodb.scala.model.UpdateOptions
 import org.mongodb.scala.bson.BsonObjectId
import example.Helpers._

object Pokedex  {
    def main(args: Array[String]){

        val fileName = "C:/Users/Work/Project0/pokedex_complete.csv"
        var bufferedSource:BufferedSource = null

            val client: MongoClient = MongoClient() //localhost:27017
            val database: MongoDatabase = client.getDatabase("poketest")
            val collection: MongoCollection[Document] = database.getCollection("pokedex")
        
        try{
            bufferedSource = Source.fromFile(fileName)
            for (line <- bufferedSource.getLines) {
                val cols = line.split(",").map(_.trim)
                //Do processing of input here
                println(s"${cols(0)}|${cols(1)}|${cols(2)}|${cols(3)}|${cols(4)}|${cols(5)}|${cols(6)}")
                val doc: Document = Document(
                    "_id" -> {cols(0)}.toInt,
                    "name" -> {cols(1)}.toUpperCase(),
                    "type1" -> {cols(2)}.toUpperCase(),
                    "type2" -> {cols(3)}.toUpperCase(),
                    "height" -> {cols(4)}.toInt,
                    "weight" -> {cols(5)}.toDouble,
                    "caught" -> {cols(6)}.toBoolean,
                )

                val observable: Observable[Completed] = collection.insertOne(doc)
                observable.subscribe(new Observer[Completed] {
                    override def onNext(result: Completed): Unit = println("Inserted")
                    override def onError(e: Throwable): Unit = println("Failed")
                    override def onComplete(): Unit = println("Completed")
                })

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

            Thread.sleep(1000)
            println("Welcome to the Pokedex! Please choose an operation:\n" +
              "(1) Search by Name\n" +
              "(2) Search by Number\n" +
              "(3) Search by Type\n" +
              "(4) Catch/Uncatch a Pokemon\n" +
              "(5) Show Caught Pokemon\n" +
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
                    pokemonToCatch = {readLine()}.toUpperCase()
                    collection.updateOne(equal("name", pokemonToCatch), set("caught", true)).printHeadResult("Update Result: ")
                    println(s"You have caught: $pokemonToCatch!") //Processing goes here
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