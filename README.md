# yipuran-jack
Jackson Java JSON-processorを使用する時の補助を目的したライブラリ

## 変更履歴
1.1 JsonClip を追加

## Document
[Wiki Page](../../wiki)

## Setup pom.xml
```
<repositories>
   <repository>
      <id>yipuran-core</id>
      <url>https://raw.github.com/yipuran/yipuran-jack/mvn-repo</url>
   </repository>
</repositories>

<dependencies>
    <dependency>
        <groupId>org.yipuran.json</groupId>
        <artifactId>yipuran-jack</artifactId>
        <version>1.1</version>
    </dependency>
</dependencies>

```


## Setup gradle
```
repositories {
    mavenCentral()
    maven { url 'https://raw.github.com/yipuran/yipuran-jack/mvn-repo'  }
}

dependencied {
    compile 'org.yipuran.json:yipuran-jack:1.1'
}
```


