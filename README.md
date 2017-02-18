# The Craftix Server

The Craftix Server is a micro Websocket server launched by [the wrapper](https://github.com/craftix/craftix-wrapper) or [the cli](https://github.com/craftix/craftix-cli) when testing.
The launcher will connect to it, and will send him signals to auth/update/launch the game, then the server will send a response to the launcher.

It makes the launcher able to launch Minecraft using java libraries.