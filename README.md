# PackageViewer
![build Status](https://github.com/ThomasRubini/PackageViewer/actions/workflows/ci.yml/badge.svg)

## What is this ?

PackageViewer is a project that allow people to search and visualize the dependencies tree of a package, for a given distribution

## Supported distributions

- Arch Linux (official repositories only, not AUR)
- Fedora (rawhide)

## How to use

You can either locally clone and compile the project, or use the [latest build](https://nightly.link/ThomasRubini/PackageViewer/workflows/ci/main/PackageViewer%20jar.zip).

### Syntax

| parameter                    | description                                     |
|------------------------------|-------------------------------------------------|
| --help, -h                   | Show help                                       |
| --distro,-d <{distribution}> | (optional) Distribution to search the packet in |

### Examples
```java -jar PackageViewer.jar neofetch```


```java -jar PackageViewer.jar -d arch xorg-server```

## Build

To build the project, you will need at least Java 15

After cloning the repository, run `./gradlew jar` to generate the jar

It will be generated in `build/libs/`.

You can launch tests with the task "test" : ./gradlew test`

## F.A.Q

Q: Do I need Internet to use this ?
A: yes, the project is using the distributions API to get package information.

Q: Will {You beloved distribution} be supported in the future ?
A: Depends on the price you are willing to pay.

Q: Can I use this on my Nintendo Switch ?
A: Yes, the Ubuntu version installed by switchroot has a recent enough version of java to run the program.

## Contributors

[CAPELIER Marla](https://github.com/Capelier-Marla)

[SIMAILA Djalim](https://github.com/DjalimSimaila)

[RUBINI Thomas](https://github.com/ThomasRubini)
