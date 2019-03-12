1、引入aar: 在module的libs目录下加入如下文件
Elastos.ORG.Did.Lib-vx.x.x.aar
Elastos.ORG.Wallet.Lib-vx.x.x.aar
customdialog.aar

2、在module的build.gradle文件中添加依赖(其中x.x.x改为实际值)
    implementation 'com.android.support:design:26.1.0'
    implementation 'com.github.qingmei2:QrCodeScannerView-Android:1.1.2'
    implementation 'com.google.zxing:core:3.3.0'
    implementation(name: 'Elastos.ORG.Wallet.Lib-vx.x.x', ext: 'aar')
    implementation(name: 'Elastos.ORG.Did.Lib-vx.x.x', ext: 'aar')
    implementation(name: 'customdialog', ext: 'aar')
注意：若项目中已经存在某条依赖，则可以忽略；如果项目中有用到其他版本的support库导致冲突，可以
在build.gradle中指定某一版本，参见demo工程中的sdk_demo中build.gradle的configurations.all配置。

3、在项目project的build.gradle文件中添加Maven依赖，若已添加，则忽略。
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}

4、Clean Project ---> Rebuild Project

5、如果build时AndroidManifefst合并出错，可以参考demo工程在<Application>标签中添加
tools:replace="android:label"属性和android:label="@string/demo_name"属性，其中
android:label="xxx"的值可以自定义。

6、应用启动时候调用        DidEntry.init(Context context);

7、需要打开SDK内置界面的时候调用   DidEntry.launch(Context context);

8、api文档参见：https://walletbrowserandroid.readthedocs.io/en/latest/api_guide.html
http请求端口号：34561。

9、demo参见demo工程；
javaScript使用方法参见src\main\assets\apidemo.html；
浏览器集成参见com.ela.wallet.sdk.demo.MainActivity.

