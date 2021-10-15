package pokedex

import scala.io.StdIn._
import scala.io.Source
import java.io.{FileNotFoundException, IOException}
import scala.io.BufferedSource
import org.mongodb.scala._
 import org.mongodb.scala.model._
 import org.mongodb.scala.model.Filters._
 import org.mongodb.scala.model.Updates._
 import org.mongodb.scala.model.Projections._
 import org.mongodb.scala.model.Sorts._
 import org.mongodb.scala.model.UpdateOptions
 import org.mongodb.scala.bson.BsonObjectId
import example.Helpers._

object Pokedex  {
    def main(args: Array[String]){

        val fileName = "C:/Users/Work/Project0/pokedex_complete.csv"
        //var bufferedSource:BufferedSource = null
        //DB Connection
        val client: MongoClient = MongoClient() //localhost:27017
        val database: MongoDatabase = client.getDatabase("poketest")
        val collection: MongoCollection[Document] = database.getCollection("pokedex")
        var loop = true

        importFileToDB(fileName, collection)
        
        do {
            var selection = -1
            var nameToSearch = ""
            var numberToSearch = 0
            var singleTypeToSearch = ""
            var doubleTypeToSearch1 = ""
            var doubleTypeToSearch2 = ""
            var pokemonNameToCatch = ""
            var pokemonNumberToCatch = 0
            loop = true

            Thread.sleep(1000)
            println("Welcome to the Pokedex! Please choose an operation:\n" +
              "(1) Find by Name (Exact Match)\n" +
              "(2) Find by Number (Exact Match)\n" +
              "(3) Search by One Type\n" +
              "(4) Search by Two Types\n" +
              "(5) Catch a Pokemon by Name\n" +
              "(6) Catch a Pokemon by Number\n" +
              "(7) Show Caught Pokemon\n" +
              "(8) Show Entire Pokedex\n" +
              "(9) Rebuild Pokedex\n" +
              "(0) Exit Pokedex")
            try {
                selection = readInt()
                selection match {
                case 1 => {
                    println("Enter Pokemon name to search: ")
                    nameToSearch = {readLine()}.trim().toUpperCase()
                    //Processing goes here
                    println(s"Here is the Pokedex entry for $nameToSearch:")
                    collection.find(equal("name", nameToSearch))
                    .sort(ascending("_id"))
                    .printResults()
                }
                case 2 => {
                    println("Enter Pokemon number to search: ")
                    numberToSearch = readInt()
                    //Processing goes here
                    println(s"Here is the Pokedex entry for Pokemon #$numberToSearch:")
                    collection.find(equal("_id", numberToSearch))
                    .sort(ascending("_id"))
                    .printResults()
                }
                case 3 => {
                    println("Enter Pokemon type to search: ")
                    singleTypeToSearch = {readLine()}.trim().toUpperCase()
                    if(checkType(singleTypeToSearch)){
                        //Processsing goes here
                        println(s"All $singleTypeToSearch type Pokemon:") 
                        collection.find(or(equal("type1", singleTypeToSearch), equal("type2", singleTypeToSearch)))
                        .sort(ascending("_id"))
                        .printResults()
                    } else println(s"Invalid type entered: $singleTypeToSearch")
                }
                case 4 => {
                    println("Enter first type to search: ")
                    doubleTypeToSearch1 = {readLine()}.trim().toUpperCase()
                    if(checkType(doubleTypeToSearch1)){
                        println("Enter second type to search: ")
                        doubleTypeToSearch2 = {readLine()}.trim().toUpperCase()
                        if(checkType(doubleTypeToSearch2)){
                            //Processing goes here
                            println(s"All $doubleTypeToSearch1/$doubleTypeToSearch2 type Pokemon:")
                            collection.find(or(and(equal("type1", doubleTypeToSearch1), equal("type2", doubleTypeToSearch2)), and(equal("type1", doubleTypeToSearch2), equal("type2", doubleTypeToSearch1))))
                            .sort(ascending("_id"))
                            .printResults() 
                        } else println(s"Invalid type entered: $doubleTypeToSearch2")
                    } else println(s"Invalid type entered: $doubleTypeToSearch1") 
                }
                case 5 => {
                    println("Enter the name of the Pokemon that you caught: ")
                    //Processing goes here
                    pokemonNameToCatch = {readLine()}.trim().toUpperCase()
                    collection.updateOne(equal("name", pokemonNameToCatch), set("caught", true)).printHeadResult("Update Result: ")
                    println(s"You have caught: $pokemonNameToCatch!") 
                }
                case 6 => {
                    println("Enter the number of the Pokemon that you caught: ")
                    //Processing goes here
                    pokemonNumberToCatch = readInt()
                    collection.updateOne(equal("_id", pokemonNumberToCatch), set("caught", true)).printHeadResult("Update Result: ")
                    println(s"You have caught Pokemon #$pokemonNumberToCatch!") 
                }
                case 7 => {
                    println("Pokemon that you have caught:")
                    //Processing goes here
                    collection.find(equal("caught", true))
                    .sort(ascending("_id"))
                    .printResults()
                }
                case 8 => {
                    println("Here is the entire Pokedex:")
                    //Processing goes here
                    collection.find(Document())
                    .sort(ascending("_id"))
                    .printResults()
                }
                case 9 => {
                    println("This will delete all changes made, are you sure you wish to do this? Enter yes (y) or no (n)")
                    var confirmation = {readLine()}.trim().toUpperCase()
                    confirmation match {
                        case "YES" | "Y" => rebuildPokedex()
                        case "NO" | "N" => println("Pokedex rebuild canceled, returning to main menu.")
                        case _ => println("Invalid input, returning to main menu")
                    }
                }
                case 0 => {
                    println("Thank you for using the Pokedex!")
                    Thread.sleep(500)
                    loop = false
                }
                case _ => {
                    println("Invalid selection, please try again.")
                }
            }
            }
            catch {
                case e: Exception => println("Invalid input, please try again.")
            }
            
            }
        while(loop)
        client.close()

        def checkType(givenType: String) : Boolean = {
            givenType.trim.toUpperCase match {
                case "NORMAL" | "FIRE" | "WATER" | "GRASS" | "ELECTRIC" | "ICE" | "FIGHTING" | "POISON" | "GROUND" | "FLYING" | "PSYCHIC" | "BUG" | "ROCK" | "GHOST" | "DARK" | "DRAGON" | "STEEL" | "FAIRY" | "NONE" => return true
                case _ => return false
            }
        }

        def rebuildPokedex() {
            collection.deleteMany(Document()).printHeadResult()
            importFileToDB(fileName, collection)
        }
        def importFileToDB(fileName: String, collection: MongoCollection[Document]) : Unit = {
            var bufferedSource:BufferedSource = null
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
        }
    }
}
