[![Maven metadata URL](https://img.shields.io/maven-metadata/v?color=blue&metadataUrl=https://s01.oss.sonatype.org/service/local/repo_groups/public/content/cafe/adriel/bonsai/bonsai-core/maven-metadata.xml&style=for-the-badge)](https://repo.maven.apache.org/maven2/cafe/adriel/bonsai/)
[![Android API](https://img.shields.io/badge/api-21%2B-brightgreen.svg?style=for-the-badge)](https://android-arsenal.com/api?level=21)
[![kotlin](https://img.shields.io/github/languages/top/adrielcafe/bonsai.svg?style=for-the-badge&color=blueviolet)](https://kotlinlang.org/)
[![ktlint](https://img.shields.io/badge/code%20style-%E2%9D%A4-FF4081.svg?style=for-the-badge)](https://ktlint.github.io/)
[![License MIT](https://img.shields.io/github/license/adrielcafe/voyager.svg?style=for-the-badge&color=orange)](https://opensource.org/licenses/MIT)

<h1 align="center">
    <img height="80" src="https://user-images.githubusercontent.com/2512298/163075335-494c2a4d-e446-4627-8865-183683a75834.png"/>
    <br>
    Bonsai
</h1>

<h3  align="center">
    A batteries-included Tree View for Jetpack Compose
    <br><br>
    <img width=400 src="https://user-images.githubusercontent.com/2512298/163289815-d584327c-ee5b-4f58-835c-121585b81f37.gif">
</h3>

#### Features
- [x] Multiplatform: Android, Desktop
- [x] State-aware: changes in the tree will trigger recomposition
- [x] Unlimited depth
- [x] Lazy loaded nodes
- [x] Survives activity recreation
- [x] [Built-in DSL](#usage)
- [x] [File System integration](#file-system-integration)
- [x] [JSON integration](#json-integration)
- [x] [Expandable](#expanding--collapsing)
- [x] [Selectable](#selecting)
- [x] [Clickable](#click-handling)
- [x] [Styleable](#styling)
- [x] [Extendable](#custom-nodes)

#### Roadmap
- iOS support
- Draggable nodes
- FileObserver (Android) and/or WatchService (JVM) integration

## Import to your project
Add the desired dependencies to your module's `build.gradle`:
```gradle
implementation "cafe.adriel.bonsai:bonsai-core:${latest-version}"
implementation "cafe.adriel.bonsai:bonsai-file-system:${latest-version}"
implementation "cafe.adriel.bonsai:bonsai-json:${latest-version}"
```

Current version: ![Maven metadata URL](https://img.shields.io/maven-metadata/v?color=blue&metadataUrl=https://s01.oss.sonatype.org/service/local/repo_groups/public/content/cafe/adriel/bonsai/bonsai-core/maven-metadata.xml)

## Usage
Bonsai comes with a handy DSL for creating high-performance, customizable trees:
1. Start by creating a new tree with `Tree<T>{}`
2. Create nodes with `Leaf<T>()` and `Branch<T>()`
3. Call `Bonsai()` to render the tree
```kotlin
@Composable
fun BonsaiExample() {
    val tree = Tree {
        Branch("Mammalia") {
            Branch("Carnivora") {
                Branch("Canidae") {
                    Branch("Canis") {
                        Leaf("Wolf", customIcon = { EmojiIcon("🐺") })
                        Leaf("Dog", customIcon = { EmojiIcon("🐶") })
                    }
                }
                Branch("Felidae") {
                    Branch("Felis") {
                        Leaf("Cat", customIcon = { EmojiIcon("🐱") })
                    }
                    Branch("Panthera") {
                        Leaf("Lion", customIcon = { EmojiIcon("🦁") })
                    }
                }
            }
        }
    }

    Bonsai(tree)
}
```

Output:

<img width=200 src="https://user-images.githubusercontent.com/2512298/164339464-2fc48395-8b52-414d-93d0-b4829dcc5c76.png">

**Take a look at the [sample app](https://github.com/adrielcafe/bonsai/blob/main/sample/src/main/java/cafe/adriel/bonsai/sample/) for working examples.**

### File System integration
Import `cafe.adriel.bonsai:bonsai-file-system` module to use it.

```kotlin
val tree = FileSystemTree(
    // Also works with java.nio.file.Path and okio.Path
    rootPath = File(path),
    // To show or not the root directory in the tree
    selfInclude = true
)

Bonsai(
    tree = tree,
    // Custom style
    style = FileSystemBonsaiStyle()
)
```

Output:

<img width=350 src="https://user-images.githubusercontent.com/2512298/164344584-d17d3131-6515-4d76-b7c1-017e33731c6f.png">

### JSON integration
Import `cafe.adriel.bonsai:bonsai-json` module to use it.

```kotlin
val tree = JsonTree(
    // Sample JSON from https://gateway.marvel.com/v1/public/characters
    json = responseJson
)

Bonsai(
    tree = tree,
    // Custom style
    style = JsonBonsaiStyle()
)
```

Output:

<img width=400 src="https://user-images.githubusercontent.com/2512298/164342551-b5f869eb-a2fd-414c-b856-916601e2785e.png"> <img width=400 src="https://user-images.githubusercontent.com/2512298/164344465-6a0fb267-cd26-458c-a67b-db18d6a45d03.png">

### Expanding & Collapsing
Easily control the expanded/collapsed state of your `Tree`: 
* `toggleExpansion(node)`
* `collapseRoot()` / `expandRoot()`
* `collapseAll()` / `expandAll()`
* `collapseFrom(depth)` / `expandUntil(depth)`
* `collapseNode(node)` / `expandNode(node)`

### Selecting
Selected/Unselected state is also pretty simple to control: 
* `selectedNodes`
* `toggleSelection(node)`
* `selectNode(node)` / `unselectNode(node)`
* `clearSelection()`

### Click handling
Its also possible to set custom click behaviors for your `Tree`. Control single, double and long clicks by using the [expand](#expanding--collapsing) and [select](#selecting) APIs.
```kotlin
Bonsai(
    tree = tree,
    onClick = { node ->
        tree.clearSelection()
        tree.toggleExpansion(node)
    },
    onDoubleClick = { node -> /* ... */ },
    onLongClick = { node -> /* ... */ }
)
```

### Styling
Change your `Tree` appearance as you wish. Take a look at `BonsaiStyle` class for all available customizations.
```kotlin
Bonsai(
    tree = tree,
    style = BonsaiStyle(
        toggleIconRotationDegrees = 0f,
        toggleIcon = { node ->
            rememberVectorPainter(
                if (node is BranchNode && node.isExpanded) Icons.Outlined.UnfoldLess
                else Icons.Outlined.UnfoldMore
            )
        },
        nodeIconSize = 18.dp,
        nodeShape = CutCornerShape(percent = 20),
        nodeCollapsedIcon = { rememberVectorPainter(Icons.Outlined.Circle) },
        nodeExpandedIcon = { rememberVectorPainter(Icons.Outlined.Adjust) },
        nodeNameTextStyle = MaterialTheme.typography.overline
    )
)
```

Output:

<img width=250 src="https://user-images.githubusercontent.com/2512298/164343259-0df574f6-e602-4bb1-93e2-c263fb1ee1ed.png">

### Custom nodes
Need a deeper customization? You can set `customIcon`s and `customName`s for each `Leaf<T>()` and `Branch<T>()` nodes.
```kotlin
Leaf(
    content = "Wolf", 
    customIcon = { EmojiIcon("🐺") }
)
```

Output:

<img width=200 src="https://user-images.githubusercontent.com/2512298/164343479-fd6d2ce0-4335-48ac-90a0-0a93c2c57649.png">