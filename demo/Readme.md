1、引入aar: 在module的libs目录下加入didlibrary-x.x.x.aar

2、在module的build.gradle文件中添加依赖
 implementation(name: 'didlibrary-x.x.x', ext: 'aar')
注：x.x.x表示版本号，需改为实际值。例如：
 implementation(name: 'didlibrary-1.0.0', ext: 'aar')

3、Clean Project ---> Rebuild Project

4、应用启动时候调用        DidEntry.init(Context context);

5、需要打开SDK内置界面的时候调用   DidEntry.launch(Context context);

6、api文档参见：https://walletbrowserandroid.readthedocs.io/en/latest/api_guide.html

7、demo参见demo工程；
javaScript使用方法参见src\main\assets\apidemo.html；
浏览器集成参见com.ela.wallet.sdk.demo.MainActivity.

