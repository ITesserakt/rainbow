[![Rainbow stats on Discord Bot List](https://discordbotlist.com/bots/481165004612173824/widget)](https://discordbotlist.com/bots/481165004612173824")

# Rainbow bot [![Codacy Badge](https://api.codacy.com/project/badge/Grade/3533cb9cd55b45ca95a5740b7949d1f7)](https://www.codacy.com/app/potryas85/rainbow?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=ITesserakt/rainbow&amp;utm_campaign=Badge_Grade) 
### Based on [Discord4J v3.0.1](https://github.com/Discord4J/Discord4J "A fast and reactive JVM-based REST/WS wrapper for the official Discord Bot API, written in Java")

>Bot\`s state is ALPHA! So, if you have a problem, report an issue on github

The main feature is rainbow roles

All commands can be viewed from `!help` command. `!help [command]` will show you a summary for command

If you want to get a rainbow on role, then just place bot\`s role above

## Defining your own command
 
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

There is also a special annotation for parameters: `@Continuous`. 
It indicates that the argument will be expanded to the end of the input.

Context variable is available only from function annotated with `@Command`.

```kotlin
class EchoModule : ModuleBase<GuildCommandContext>() {
    @Command
    @Summary("the simplest command that reprints your last message")
    suspend fun echo(@Continuous msg : String) {
        context.reply(msg)
    }
}
```

## Defining your own handler

This can be done in 2 ways:
1. with a lambda, but we can\`t unsubscribe:
```kotlin
discordClient.eventDispatcher.on<ReadyEvent>() += {
    //code
}
```
2. with a handler instance:
```kotlin
class ReadyHandler : Handler<ReadyEvent>() {
    override suspend fun handle(event : ReadyEvent) {
        //code
    }
}

discordClient.eventDispatcher.on<ReadyEvent>() += ReadyHandler()
//...
discordClient.eventDispatcher.on<ReadyEvent>() -= ReadyHandler()
```

## Auto loading of commands

It\`s possible to load all commands from a specified package using `DiscordClient.commandLoader.load()`. As arguments you have to pass providers for contexts.
> by default, the entire jar file is viewed, which can be slow for a large project, so you can indicate from which package commands are loaded. In DiscordClientBuilder use `commandLoadersPackage`
