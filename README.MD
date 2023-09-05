# Aurora

Open source Java based messenger.
Made with :heart:

## Building

To build runnable jar run `./gradlew bootJar` / `gradlew.bat bootJar`.

To generate code for the database use `codegen` task. 
Before generating do not forget to put necessary database information into `database.json`.
See `database.example.json`

## TODO

### Server
- [ ] Improve server
- [ ] Make API error status codes

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
