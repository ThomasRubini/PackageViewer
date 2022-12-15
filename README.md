# PackageViewer
![build Status](https://github.com/ThomasRubini/PackageViewer/actions/workflows/ci.yml/badge.svg)

## A propos

PackageViewer permer de rechercher et de visualiser un paquet et les dependances du paquet pour une distribtion donnée.

## Distribution supportées

- Arch Linux (depots officiels)
- Fedora

## Utilisation

Pour utiliser notre projet vous pouvez cloner et build notre projet ou bien recuperer la [derniere build](https://nightly.link/ThomasRubini/PackageViewer/workflows/ci/main/PackageViewer%20jar.zip).

### parametres

|  parametre  | description                                      |
| ----------- | ------------------------------------------------ |
| --help, -h  | Affiche l’aide                                   |
| --distro,-d <{distribution}> | Distribution linux dans la quelle rechercher le paquet |
| --depth  <{profondeur}>  | Profondeur de l’arbre de dependance a afficher   | 

### Exemples
```java -jar PackageViewer.jar -d fedora neofetch```


```java -jar PackageViewer.jar -d arch --depth 6 xorg-server```

## Build

Pour build le projet il faut necessairement une version de java superieure a 15.

Pour créer le fichier jar de notre projet il suffit de lancer la task "jar" du projet gradle :

```./gradlew jar```

Vous retrouverez le fichier jar dans le dossier ```build/libs/```.

Vous pouvez lancer les test en lançant la task "test" :

```./gradlew test```

Dans le cas ou vous n'avez jamais utilisé gradle, l'executable ```gradlew``` se trouve a la racine du depot.

## F.A.Q

Q: Est ce qu'il me faut internet pour utiliser ce programme?
R: Oui, il fonctionne en faisant des requetes vers des api, sans connection c'est un peu plus compliqué.

Q: Est ce que {votre distribution preferée} sera supportée dans le futur?
R: Si elle fournit une api pour ses paquets, peut etre. Sinon non :).

Q: Est ce que je peux l'utiliser sur ma Nintendo Switch?
R: Oui, la version d'ubuntu que switchroot installe a une version assez recente de java pour lancer le programme.

## Contributeurs

[CAPELIER Marla](https://github.com/Capelier-Marla)

[SIMAILA Djalim](https://github.com/DjalimSimaila)

[RUBINI Thomas](https://github.com/ThomasRubini)
