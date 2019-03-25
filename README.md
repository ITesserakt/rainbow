<a href="https://discordbotlist.com/bots/481165004612173824">
    <img 
        width="380" 
        height="150" 
        src="https://discordbotlist.com/bots/481165004612173824/widget" 
        alt="Rainbow stats on Discord Bot List">
</a>

#Rainbow bot

[![Codacy Badge](https://api.codacy.com/project/badge/Grade/c098bd0a5ceb447e9fabe025e6942cac)](https://app.codacy.com/app/potryas85/rainbow?utm_source=github.com&utm_medium=referral&utm_content=ITesserakt/rainbow&utm_campaign=Badge_Grade_Dashboard)

>Bot\`s state is ALPHA! So, if you have a problem, report an issue on github

The main feature is rainbow roles

All commands can be viewed from `!help` command. `!help [command]` will show you a summary for command

If you want to get a rainbow on role, then just place bot\`s role above

##Defining your own command
 
Create new class, which extended by ModuleBase. Its prefer to name class using postfix `Module`.
Also, as a type parameter of ModuleBase you have to provide your type of context 
```kotlin
class EchoModule : ModuleBase<GuildCommandContext>()
``` 
Commands are simple functions with some magic annotations, such as 
* Command (defines a new command)
* Summary (adds a description)
* Permissions (defines who can run your command; works only in guild context)
* Hidden (hides the command from `help` output but it\`s can still be run)
* Aliases
* Require...

There is also a special annotation for parameters: `continuous`. 
It indicates that the argument will be expanded to the end of the input.

```kotlin
class EchoModule : ModuleBase<GuildCommandContext>() {
    @Command
    @Summary("the simplest command that reprints your last message")
    suspend fun echo() {
        context.reply(context.message.content.get())
    }
}
```

##Defining your own handler

Extend your class with Handler\<T : Event\> and do whatever you want
```kotlin
class ReadyEventHandler : Handler<ReadyEvent>() {
    override fun handle(event : ReadyEvent) = yourCoroutineScope.lauch {
        /*
         *   code
        */
    }
}
```