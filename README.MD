# Aurora

Open source Java based messenger.
Made with :heart:

## Building

1. Make copy of `database.example.json` named `database.json` with your database credentials
2. Generate code for the database using `codegen` task
3. To build runnable jar run `./gradlew bootJar` / `gradlew.bat bootJar`.

## TODO

### Server
- [ ] Improve server
- [ ] Make API error status codes
- [ ] Server API docs

### Client
- [ ] Java multiplatform client
  - UI Framework: JavaFX
  - Icon pack: [jIconFont - Material](http://jiconfont.github.io)
- [ ] Windows
  - UI Framework: WinUI
  - Icon packs: [Fluent](https://github.com/microsoft/fluentui-system-icons) / [Segoe](https://learn.microsoft.com/en-us/windows/apps/design/style/icons)
- [ ] MacOS / iOS
  - UI Framework: SwiftUI
  - Icon pack: SFSymbols

> Some notes I wrote myself not to forget about
