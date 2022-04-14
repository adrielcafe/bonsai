[![Maven metadata URL](https://img.shields.io/maven-metadata/v?color=blue&metadataUrl=https://s01.oss.sonatype.org/service/local/repo_groups/public/content/cafe/adriel/bonsai/bonsai-core/maven-metadata.xml&style=for-the-badge)](https://repo.maven.apache.org/maven2/cafe/adriel/bonsai/)
[![Android API](https://img.shields.io/badge/api-21%2B-brightgreen.svg?style=for-the-badge)](https://android-arsenal.com/api?level=21)
[![kotlin](https://img.shields.io/github/languages/top/adrielcafe/bonsai.svg?style=for-the-badge&color=blueviolet)](https://kotlinlang.org/)
[![ktlint](https://img.shields.io/badge/code%20style-%E2%9D%A4-FF4081.svg?style=for-the-badge)](https://ktlint.github.io/)
[![License MIT](https://img.shields.io/github/license/adrielcafe/voyager.svg?style=for-the-badge&color=orange)](https://opensource.org/licenses/MIT)

<h1 align="center">
    <img height="80" src="https://user-images.githubusercontent.com/2512298/163075335-494c2a4d-e446-4627-8865-183683a75834.png"/>
    <br>
    Bonsai
    <br>
</h1>

<h3  align="center">
    A batteries-included Tree View for Jetpack Compose
    <br>
    <img width=400 src="https://user-images.githubusercontent.com/2512298/163289815-d584327c-ee5b-4f58-835c-121585b81f37.gif">
</h3>

#### Features
- [x] Multiplatform: Android, Desktop
- [x] State-aware: changes in the tree will trigger recomposition
- [x] Unlimited levels
- [x] [File system integration](#file-system)
- [x] [Built-in DSL](#dsl)
- [x] [Expandable](#expanding--collapsing)
- [x] [Selectable](#selecting)
- [x] [Clickable](#click-handling)
- [x] [Styleable](#styling)
- [x] [Extendable](#custom-nodes)

#### Roadmap
- iOS support

## Usage
Start by creating a new tree with `rememberTree<T>()`. Use `SimpleLeafNode<T>` and `SimpleBranchNode<T>` to create leaf and branch nodes, respectively. Call the `Bonsai()` composable to render the tree.
```kotlin
@Composable
fun BonsaiExample() {
    val tree = rememberTree(
        nodes = listOf(
            SimpleLeafNode(
                content = "A leaf node"
            ),
            SimpleBranchNode(
                content = "A branch node",
                children = { node ->
                    listOf(
                        SimpleLeafNode(
                            content = "A child node",
                            parent = node
                        ),
                    )
                }
            )
        )
    )

    Bonsai(tree)
}
```

Output:

<img width=150 src="https://user-images.githubusercontent.com/2512298/163285446-df635cf7-c43c-494a-be0e-a4673c7ee0eb.png">

**Take a look at the [sample app](https://github.com/adrielcafe/bonsai/blob/main/sample/src/main/java/cafe/adriel/bonsai/sample/SampleActivity.kt) for a working example.**

### File System
Bonsai is integrated with file system, you should import `bonsai-file-system` module to use it.

Instead of manually create the nodes, call `fileSystemNodes()` to generate for you based on a root path.
```kotlin
val tree = rememberTree<Path>(
    nodes = fileSystemNodes(
        // Also works with java.nio.file.Path and okio.Path
        rootPath = File(path), 
        // To show or not the root directory in the tree
        selfInclude = true 
    )
)

Bonsai(
    tree = tree,
    // Custom style to show file and directory icons
    style = FileSystemBonsaiStyle()
)
```

Output:

<img width=300 src="https://user-images.githubusercontent.com/2512298/163184371-a5a38003-44d9-4daa-8f41-6ee3914611f1.png">

### DSL
Looking for a simpler and less verbose way to create a tree? Here's a handy DSL for you.
```kotlin
val tree = Tree<String> {
    Branch("bonsai") {
        Branch("bonsai-core") {
            Branch("build")
            Branch("src") {
                Branch("androidMain") {
                    Leaf("AndroidManifest.xml")
                }
                Branch("commonMain")
            }
            Leaf("build.gradle")
        }
        Branch("bonsai-file-system")
        Branch("sample")
        Leaf(".gitignore")
        Leaf("build.gradle")
        Leaf("README.md")
    }
}
```

Output:

<img width=300 src="https://user-images.githubusercontent.com/2512298/163077920-a9c29430-544a-4ab3-94f8-250a6028638d.png">

### Expanding & Collapsing
Easy control the expanded/collapsed state of your `Tree`: 
* `toggleExpansion(node)`
* `collapseAll()` / `expandAll()`
* `collapseRoot()` / `expandRoot()`
* `collapseFrom(minLevel)` / `expandUntil(maxLevel)`
* `collapseNode(node)` / `expandNode(node)`

### Selecting
Selected/Unselected state is also pretty simple to control: 
* `selectedNodes` (observable state backed by `SnapshotStateList`)
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
        expandTransition = slideIn(),
        collapseTransition = slideOut(),
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

<img width=300 src="https://user-images.githubusercontent.com/2512298/163079810-dfa5b4be-9111-4de1-a3eb-81bf8440963a.png">

### Custom nodes
Need a deeper customization? Just extend the `SimpleLeafNode` and `SimpleBranchNode` classes, or even the `Node` interface, to fit your needs.
```kotlin
class CustomLeafNode : SimpleLeafNode<CustomClass>(/* setup */) {

    @Composable
    override fun BonsaiScope<T>.NodeIcon() {
        // Custom leaf node icon
    }

    @Composable
    override fun BonsaiScope<T>.NodeName() {
        // Custom leaf node name
    }
}

class CustomBranchNode : SimpleBranchNode<CustomClass>(/* setup */) {

    @Composable
    override fun BonsaiScope<T>.NodeIcon() {
        // Custom branch node icon
    }

    @Composable
    override fun BonsaiScope<T>.NodeName() {
        // Custom branch node name
    }
}
```

## Import to your project
Add the desired dependencies to your module's `build.gradle`:
```gradle
implementation "cafe.adriel.bonsai:bonsai-core:${latest-version}"
implementation "cafe.adriel.bonsai:bonsai-file-system:${latest-version}"
```

Current version: ![Maven metadata URL](https://img.shields.io/maven-metadata/v?color=blue&metadataUrl=https://s01.oss.sonatype.org/service/local/repo_groups/public/content/cafe/adriel/bonsai/bonsai-core/maven-metadata.xml)
