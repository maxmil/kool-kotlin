package my.favorite.kotlin.features

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.node.TextNode
import my.favorite.kotlin.features.ClassDelegates.*
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatExceptionOfType
import org.junit.Test
import java.util.concurrent.Callable
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException


/*



 ________              __                                    __
|        \            |  \                                  |  \
| $$$$$$$$ __    __  _| $$_     ______   _______    _______  \$$  ______   _______
| $$__    |  \  /  \|   $$ \   /      \ |       \  /       \|  \ /      \ |       \
| $$  \    \$$\/  $$ \$$$$$$  |  $$$$$$\| $$$$$$$\|  $$$$$$$| $$|  $$$$$$\| $$$$$$$\
| $$$$$     >$$  $$   | $$ __ | $$    $$| $$  | $$ \$$    \ | $$| $$  | $$| $$  | $$
| $$_____  /  $$$$\   | $$|  \| $$$$$$$$| $$  | $$ _\$$$$$$\| $$| $$__/ $$| $$  | $$
| $$     \|  $$ \$$\   \$$  $$ \$$     \| $$  | $$|       $$| $$ \$$    $$| $$  | $$
 \$$$$$$$$ \$$   \$$    \$$$$   \$$$$$$$ \$$   \$$ \$$$$$$$  \$$  \$$$$$$  \$$   \$$
  ______                                   __      __
 /      \                                 |  \    |  \
|  $$$$$$\ __    __  _______    _______  _| $$_    \$$  ______   _______    _______
| $$_  \$$|  \  |  \|       \  /       \|   $$ \  |  \ /      \ |       \  /       \
| $$ \    | $$  | $$| $$$$$$$\|  $$$$$$$ \$$$$$$  | $$|  $$$$$$\| $$$$$$$\|  $$$$$$$
| $$$$    | $$  | $$| $$  | $$| $$        | $$ __ | $$| $$  | $$| $$  | $$ \$$    \
| $$      | $$__/ $$| $$  | $$| $$_____   | $$|  \| $$| $$__/ $$| $$  | $$ _\$$$$$$\
| $$       \$$    $$| $$  | $$ \$$     \   \$$  $$| $$ \$$    $$| $$  | $$|       $$
 \$$        \$$$$$$  \$$   \$$  \$$$$$$$    \$$$$  \$$  \$$$$$$  \$$   \$$ \$$$$$$$



*/

















fun String.echo(): Unit{
    println(this)
}































data class Person(val name: String)

val node = TextNode("bob")

fun toPerson(node: JsonNode) = Person(node.asText())
fun isCalledBob(person: Person) = person.name == "bob"

val isBob = isCalledBob(toPerson(node))


























class LeftToRight {

    fun JsonNode.toPerson() = Person(asText())
    fun Person.isCalledBob() = name == "bob"

    val isBob = node.toPerson().isCalledBob()

    @Test
    fun `Left to right gives the same result`() {
        assertThat(LeftToRight().isBob).isEqualTo(isBob)
    }
}






















/*

javap build.kotlin-classes.test.my.favorite.kotlin.features.LeftToRight
Warning: Binary file build.kotlin-classes.test.my.favorite.kotlin.features.LeftToRight contains my.favorite.kotlin.features.LeftToRight
Compiled from "Presentation.kt"
public final class my.favorite.kotlin.features.LeftToRight {
  public final my.favorite.kotlin.features.Person toPerson(com.fasterxml.jackson.databind.JsonNode);
  public final boolean isCalledBob(my.favorite.kotlin.features.Person);
  public final boolean isBob();
  public final void Left to right gives the same result();
  public my.favorite.kotlin.features.LeftToRight();
}

*/

















class ExtensionFunctionsCanExtendFunctions {

    fun (() -> String).timeBoxed(timeout: Long, unit: TimeUnit): () -> String = {
        Executors.newSingleThreadExecutor().submit(Callable { this() }).get(timeout, unit)
    }

    val waitForOneSecondAndThenSayHi: () -> String = {
        Thread.sleep(1000)
        "Hi"
    }

    @Test
    fun `Times out when timeBoxed 100 milliseconds`() {
        val timeBoxed100Millis = waitForOneSecondAndThenSayHi.timeBoxed(100, TimeUnit.MILLISECONDS)
        assertThatExceptionOfType(TimeoutException::class.java).isThrownBy { timeBoxed100Millis() }
    }

    @Test
    fun `Does not time out when timeBoxed 2 seconds`() {
        val timeBoxed2Seconds = waitForOneSecondAndThenSayHi.timeBoxed(2, TimeUnit.SECONDS)
        assertThat(timeBoxed2Seconds()).isEqualTo("Hi")
    }
}

/*




 __    __            __  __            __        __
|  \  |  \          |  \|  \          |  \      |  \
| $$\ | $$ __    __ | $$| $$  ______  | $$____  | $$  ______
| $$$\| $$|  \  |  \| $$| $$ |      \ | $$    \ | $$ /      \
| $$$$\ $$| $$  | $$| $$| $$  \$$$$$$\| $$$$$$$\| $$|  $$$$$$\
| $$\$$ $$| $$  | $$| $$| $$ /      $$| $$  | $$| $$| $$    $$
| $$ \$$$$| $$__/ $$| $$| $$|  $$$$$$$| $$__/ $$| $$| $$$$$$$$
| $$  \$$$ \$$    $$| $$| $$ \$$    $$| $$    $$| $$ \$$     \
 \$$   \$$  \$$$$$$  \$$ \$$  \$$$$$$$ \$$$$$$$  \$$  \$$$$$$$



 ________
|        \
 \$$$$$$$$__    __   ______    ______    _______
   | $$  |  \  |  \ /      \  /      \  /       \
   | $$  | $$  | $$|  $$$$$$\|  $$$$$$\|  $$$$$$$
   | $$  | $$  | $$| $$  | $$| $$    $$ \$$    \
   | $$  | $$__/ $$| $$__/ $$| $$$$$$$$ _\$$$$$$\
   | $$   \$$    $$| $$    $$ \$$     \|       $$
    \$$   _\$$$$$$$| $$$$$$$   \$$$$$$$ \$$$$$$$
         |  \__| $$| $$
          \$$    $$| $$
           \$$$$$$  \$$



    Tony Hoare introduced Null references in ALGOL W back in 1965,
    in his own words "simply because it was so easy to implement".

    He has now spoken about that decision as a "my billion-dollar mistake".

 */









class NullTypes {
    var nullString: String? = null
    var nonNullString: String = ""


    // They are still types with the nullable type as a super type of the null type

    val `nonNullString is a super type Of nullString`: String? = nonNullString

//    val `null string is a sub type of nonNullString`: String = nullString


    // Applies to user defined types not just platform types
}




















class ExtendNullTypes {

    fun String?.withDefault(default:String = ""):String = if (this == null) default else this

    fun List<String>?.add(s: String) = if (this == null) listOf(s) else this + s

    @Test
    fun `Default value for null string`() {
        val nullString: String? = null

        assertThat(nullString.withDefault()).isEqualTo("")
    }

    @Test
    fun `Can add to null list`() {
        val nullList: List<String>? = null

        assertThat(nullList.add("some")).isEqualTo(listOf("some"))
    }
}





















class SmartCast {

    val nullString: String? = null

    fun doTheMagic(){
        if (nullString != null) println(nullString.length)
//        println(nullString.length)
    }
}

























class NullableTypeParameters {

    fun <T : Any> notNull(thing: T) = if (thing == null) throw NullPointerException() else thing

    fun <T : Any> possiblyNull(thing: T?) = if (thing == null) throw NullPointerException() else thing
}


















/*


  ______   __
 /      \ |  \
|  $$$$$$\| $$  ______    _______   _______
| $$   \$$| $$ |      \  /       \ /       \
| $$      | $$  \$$$$$$\|  $$$$$$$|  $$$$$$$
| $$   __ | $$ /      $$ \$$    \  \$$    \
| $$__/  \| $$|  $$$$$$$ _\$$$$$$\ _\$$$$$$\
 \$$    $$| $$ \$$    $$|       $$|       $$
  \$$$$$$  \$$  \$$$$$$$ \$$$$$$$  \$$$$$$$



 _______             __                                 __
|       \           |  \                               |  \
| $$$$$$$\  ______  | $$  ______    ______    ______  _| $$_     ______    _______
| $$  | $$ /      \ | $$ /      \  /      \  |      \|   $$ \   /      \  /       \
| $$  | $$|  $$$$$$\| $$|  $$$$$$\|  $$$$$$\  \$$$$$$\\$$$$$$  |  $$$$$$\|  $$$$$$$
| $$  | $$| $$    $$| $$| $$    $$| $$  | $$ /      $$ | $$ __ | $$    $$ \$$    \
| $$__/ $$| $$$$$$$$| $$| $$$$$$$$| $$__| $$|  $$$$$$$ | $$|  \| $$$$$$$$ _\$$$$$$\
| $$    $$ \$$     \| $$ \$$     \ \$$    $$ \$$    $$  \$$  $$ \$$     \|       $$
 \$$$$$$$   \$$$$$$$ \$$  \$$$$$$$ _\$$$$$$$  \$$$$$$$   \$$$$   \$$$$$$$ \$$$$$$$
                                  |  \__| $$
                                   \$$    $$
                                    \$$$$$$




 */



class ClassDelegates {
    interface Drummer {
        fun hiphop(): String
        fun bosanova(): String
        fun rock(): String
        fun funky(): String
    }
    interface Singer {
        fun doe(): String
        fun re(): String
        fun mi(): String
        fun fa(): String
        fun sol(): String
        fun la(): String
        fun si(): String
    }
    class OneRhythmDrummer : Drummer {
        override fun hiphop() = "Boom Cha Boom Boom Cha"
        override fun bosanova() = "Boom Cha Boom Boom Cha"
        override fun rock() = "Boom Cha Boom Boom Cha"
        override fun funky() = "Boom Cha Boom Boom Cha"
    }
    class DrunkenSinger : Singer {
        override fun `doe`() = "miiiiiii"
        override fun re() = "laaaaaa"
        override fun mi() = "soooooool"
        override fun fa() = "siiiiiiii"
        override fun sol() = "dooooooo"
        override fun la() = "faaaaaaa"
        override fun si() = "reeeeeee"
    }
}


















class KotlinOneManBand : Drummer by OneRhythmDrummer(), Singer by DrunkenSinger()



















/*



 _______              __      __
|       \            |  \    |  \
| $$$$$$$\  ______  _| $$_  _| $$_     ______    ______
| $$__/ $$ /      \|   $$ \|   $$ \   /      \  /      \
| $$    $$|  $$$$$$\\$$$$$$ \$$$$$$  |  $$$$$$\|  $$$$$$\
| $$$$$$$\| $$    $$ | $$ __ | $$ __ | $$    $$| $$   \$$
| $$__/ $$| $$$$$$$$ | $$|  \| $$|  \| $$$$$$$$| $$
| $$    $$ \$$     \  \$$  $$ \$$  $$ \$$     \| $$
 \$$$$$$$   \$$$$$$$   \$$$$   \$$$$   \$$$$$$$ \$$



                                                   __
                                                  |  \
  ______    ______   _______    ______    ______   \$$  _______   _______
 /      \  /      \ |       \  /      \  /      \ |  \ /       \ /       \
|  $$$$$$\|  $$$$$$\| $$$$$$$\|  $$$$$$\|  $$$$$$\| $$|  $$$$$$$|  $$$$$$$
| $$  | $$| $$    $$| $$  | $$| $$    $$| $$   \$$| $$| $$       \$$    \
| $$__| $$| $$$$$$$$| $$  | $$| $$$$$$$$| $$      | $$| $$_____  _\$$$$$$\
 \$$    $$ \$$     \| $$  | $$ \$$     \| $$      | $$ \$$     \|       $$
 _\$$$$$$$  \$$$$$$$ \$$   \$$  \$$$$$$$ \$$       \$$  \$$$$$$$ \$$$$$$$
|  \__| $$
 \$$    $$
  \$$$$$$



 */














// The covariance problem

class InOutAndShakeItAllAbout {
    class Box<out T>

    fun covariantAssign(boxOfInteger: Box<Int>) {
        val boxOfNumber:Box<Number> = boxOfInteger
    }
}























interface EasyToReadTypeSignatures {

    // In Java for a Stream<T> the signature for flat map looks like this
    //<R> Stream<R> flatMap(Function<? super T, ? extends Stream<? extends R>> mapper);

    interface Function<in IN, out OUT>

    interface Stream<out T> {
        fun <R> flatMap(f: Function<T, R>): Stream<R>
    }
}


























class ReifiedGenerics {

    inline fun <reified T : Any> genericTypeOf(list: List<T>) = T::class

    @Test
    fun `Can get class name of generic list`() {
        assertThat(genericTypeOf(listOf("one", "two", "three"))).isEqualTo(String::class)
        assertThat(genericTypeOf(listOf(1, 2, 3))).isEqualTo(Integer::class)
    }
}
















/*

  ______     __      __                                              __                 ______    ______
 /      \   |  \    |  \                                            |  \               /      \  /      \
|  $$$$$$\ _| $$_   | $$____    ______    ______          _______  _| $$_    __    __ |  $$$$$$\|  $$$$$$\
| $$  | $$|   $$ \  | $$    \  /      \  /      \        /       \|   $$ \  |  \  |  \| $$_  \$$| $$_  \$$
| $$  | $$ \$$$$$$  | $$$$$$$\|  $$$$$$\|  $$$$$$\      |  $$$$$$$ \$$$$$$  | $$  | $$| $$ \    | $$ \
| $$  | $$  | $$ __ | $$  | $$| $$    $$| $$   \$$       \$$    \   | $$ __ | $$  | $$| $$$$    | $$$$
| $$__/ $$  | $$|  \| $$  | $$| $$$$$$$$| $$             _\$$$$$$\  | $$|  \| $$__/ $$| $$      | $$
 \$$    $$   \$$  $$| $$  | $$ \$$     \| $$            |       $$   \$$  $$ \$$    $$| $$      | $$
  \$$$$$$     \$$$$  \$$   \$$  \$$$$$$$ \$$             \$$$$$$$     \$$$$   \$$$$$$  \$$       \$$



 - Everything is an expression (except an assignment)
 - Algebraic data types
 - Type inference
 - Operator overloading
 - Smart casts
 - Named arguments
 - Default arguments
 - Data classes
 - Tail recursion
 - Delegated properties
 - Infix function notation

 */

