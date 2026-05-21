package com.codepad.app.data.model

data class Language(
    val id: String,
    val displayName: String,
    val extension: String,
    val abbreviations: List<String>,
    val defaultSnippet: String
) {
    companion object {
        val ALL: List<Language> = listOf(
            Language(
                id = "javascript",
                displayName = "JavaScript",
                extension = ".js",
                abbreviations = listOf("js", "jsx", "node", "nodejs", "ecmascript", "es6", "es2015"),
                defaultSnippet = """// Welcome to CodePad!
// Click Run to execute your code.

function greet(name) {
  return "Hello, " + name + "!";
}

console.log(greet("World"));"""
            ),
            Language(
                id = "typescript",
                displayName = "TypeScript",
                extension = ".ts",
                abbreviations = listOf("ts", "tsx"),
                defaultSnippet = """// Welcome to CodePad TypeScript!

function greet(name: string): string {
  return "Hello, " + name + "!";
}

console.log(greet("TypeScript"));"""
            ),
            Language(
                id = "python",
                displayName = "Python",
                extension = ".py",
                abbreviations = listOf("py", "python3", "py3", "pypy"),
                defaultSnippet = """a, b = 10, 20
sum = a + b

print("Hello Python!")
print("Sum =", sum)"""
            ),
            Language(
                id = "java",
                displayName = "Java",
                extension = ".java",
                abbreviations = listOf("jav"),
                defaultSnippet = """public class Main {
    public static void main(String[] args) {
        int a = 10, b = 20;
        int sum = a + b;

        System.out.println("Hello Java!");
        System.out.println("Sum = " + sum);
    }
}"""
            ),
            Language(
                id = "c",
                displayName = "C",
                extension = ".c",
                abbreviations = listOf("c-lang"),
                defaultSnippet = """#include <stdio.h>

int main() {
    int a = 10, b = 20;
    int sum = a + b;

    printf("Hello C!\n");
    printf("Sum = %d\n", sum);

    return 0;
}"""
            ),
            Language(
                id = "c++",
                displayName = "C++",
                extension = ".cpp",
                abbreviations = listOf("cpp", "cxx", "cplusplus", "c-plus-plus", "cc"),
                defaultSnippet = """#include <iostream>
using namespace std;

int main() {
    int a = 10, b = 20;
    int sum = a + b;

    cout << "Hello C++!" << endl;
    cout << "Sum = " << sum << endl;

    return 0;
}"""
            ),
            Language(
                id = "c#",
                displayName = "C#",
                extension = ".cs",
                abbreviations = listOf("csharp", "cs", "c-sharp", "dotnet"),
                defaultSnippet = """using System;

class Program {
    static void Main() {
        int a = 10, b = 20;
        int sum = a + b;

        Console.WriteLine("Hello C#!");
        Console.WriteLine("Sum = " + sum);
    }
}"""
            ),
            Language(
                id = "go",
                displayName = "Go",
                extension = ".go",
                abbreviations = listOf("golang"),
                defaultSnippet = """package main

import "fmt"

func main() {
    a, b := 10, 20
    sum := a + b

    fmt.Println("Hello Go!")
    fmt.Println("Sum =", sum)
}"""
            ),
            Language(
                id = "rust",
                displayName = "Rust",
                extension = ".rs",
                abbreviations = listOf("rs", "rustlang"),
                defaultSnippet = """fn main() {
    let a = 10;
    let b = 20;
    let sum = a + b;

    println!("Hello Rust!");
    println!("Sum = {}", sum);
}"""
            ),
            Language(
                id = "php",
                displayName = "PHP",
                extension = ".php",
                abbreviations = listOf("php7", "php8"),
                defaultSnippet = """<?php
${'$'}a = 10;
${'$'}b = 20;
${'$'}sum = ${'$'}a + ${'$'}b;

echo "Hello PHP!\n";
echo "Sum = ${'$'}sum\n";
?>"""
            ),
            Language(
                id = "ruby",
                displayName = "Ruby",
                extension = ".rb",
                abbreviations = listOf("rb"),
                defaultSnippet = """a, b = 10, 20
sum = a + b

puts "Hello Ruby!"
puts "Sum = #{sum}""""
            ),
            Language(
                id = "kotlin",
                displayName = "Kotlin",
                extension = ".kt",
                abbreviations = listOf("kt", "kts", "kotlinscript"),
                defaultSnippet = """fun main() {
    val a = 10
    val b = 20
    val sum = a + b

    println("Hello Kotlin!")
    println("Sum = ${'$'}sum")
}"""
            ),
            Language(
                id = "r",
                displayName = "R",
                extension = ".r",
                abbreviations = listOf("rlang", "r-lang", "rscript"),
                defaultSnippet = """a <- 10
b <- 20
sum <- a + b

cat("Hello R!\n")
cat("Sum =", sum, "\n")"""
            ),
            Language(
                id = "bash",
                displayName = "Bash",
                extension = ".sh",
                abbreviations = listOf("shell", "sh", "zsh", "ksh", "shellscript", "shell-script", "bash-script"),
                defaultSnippet = """#!/bin/bash

a=10
b=20
sum=${'$'}((a + b))

echo "Hello Bash!"
echo "Sum = ${'$'}sum""""
            ),
            Language(
                id = "swift",
                displayName = "Swift",
                extension = ".swift",
                abbreviations = listOf("swiftlang"),
                defaultSnippet = """let a = 10
let b = 20
let sum = a + b

print("Hello Swift!")
print("Sum = \(sum)")"""
            ),
            Language(
                id = "dart",
                displayName = "Dart",
                extension = ".dart",
                abbreviations = listOf("dartlang", "flutter"),
                defaultSnippet = """void main() {
  int a = 10;
  int b = 20;
  int sum = a + b;

  print("Hello Dart!");
  print("Sum = ${'$'}sum");
}"""
            ),
            Language(
                id = "scala",
                displayName = "Scala",
                extension = ".scala",
                abbreviations = listOf("sc"),
                defaultSnippet = """object Main extends App {
  val a = 10
  val b = 20
  val sum = a + b

  println("Hello Scala!")
  println(s"Sum = ${'$'}sum")
}"""
            ),
            Language(
                id = "perl",
                displayName = "Perl",
                extension = ".pl",
                abbreviations = listOf("pl", "perl5", "perl6"),
                defaultSnippet = """my ${'$'}a = 10;
my ${'$'}b = 20;
my ${'$'}sum = ${'$'}a + ${'$'}b;

print "Hello Perl!\n";
print "Sum = ${'$'}sum\n";"""
            ),
            Language(
                id = "lua",
                displayName = "Lua",
                extension = ".lua",
                abbreviations = listOf("luajit"),
                defaultSnippet = """local a = 10
local b = 20
local sum = a + b

print("Hello Lua!")
print("Sum = " .. sum)"""
            ),
            Language(
                id = "haskell",
                displayName = "Haskell",
                extension = ".hs",
                abbreviations = listOf("hs", "ghc"),
                defaultSnippet = """main :: IO ()
main = do
    let a = 10
        b = 20
        s = a + b
    putStrLn "Hello Haskell!"
    putStrLn ("Sum = " ++ show s)"""
            ),
            Language(
                id = "elixir",
                displayName = "Elixir",
                extension = ".ex",
                abbreviations = listOf("ex", "exs", "iex"),
                defaultSnippet = """a = 10
b = 20
sum = a + b

IO.puts("Hello Elixir!")
IO.puts("Sum = #{sum}")"""
            ),
            Language(
                id = "clojure",
                displayName = "Clojure",
                extension = ".clj",
                abbreviations = listOf("clj", "cljs", "cljc"),
                defaultSnippet = """(let [a 10
      b 20
      sum (+ a b)]
  (println "Hello Clojure!")
  (println (str "Sum = " sum)))"""
            ),
            Language(
                id = "groovy",
                displayName = "Groovy",
                extension = ".groovy",
                abbreviations = listOf("gvy", "gy"),
                defaultSnippet = """def a = 10
def b = 20
def sum = a + b

println "Hello Groovy!"
println "Sum = ${'$'}sum""""
            ),
            Language(
                id = "sql",
                displayName = "SQL",
                extension = ".sql",
                abbreviations = listOf("mysql", "postgresql", "postgres", "sqlite"),
                defaultSnippet = """-- Welcome to CodePad SQL!
SELECT 'Hello SQL!' AS greeting;
SELECT 10 + 20 AS sum;"""
            ),
        )

        private val lookupMap: Map<String, Language> by lazy {
            val map = mutableMapOf<String, Language>()
            ALL.forEach { lang ->
                map[lang.id] = lang
                map[lang.displayName.lowercase()] = lang
                lang.abbreviations.forEach { abbr -> map[abbr] = lang }
            }
            map
        }

        fun findById(id: String): Language =
            ALL.firstOrNull { it.id == id } ?: ALL.first()

        fun resolve(input: String): Language? =
            lookupMap[input.lowercase().trim()]
    }
}
