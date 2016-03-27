#データバインディング

##概要
データバインディングはロジックとビューを自動で結合してくれるライブラリで  
データに変更が起きた場合に生じるビューの変更のためのコードを削減できるようになる。  
その結果、ビューがより宣言的に記述できるようになる。

##データバインディングを使用するには
###要件 
* Android 2.1以上
* Gradle 1.5.0以上  
* Android Studio1.3以上  
  
###導入
build.gradleに以下を足すだけ!
```
android {
    ....
    dataBinding {
        enabled = true
    }
}
```
##データバインディングの使い方
### レイアウトファイル (View)
* ルートタグは<layout\>
* <data></data>の間にバインドするモデル (バインドするデータを表すオブジェクト)を記述
* レイアウトの各属性値に@{バインドするフィールド名}の形式でバインドする
```
<?xml version="1.0" encoding="utf-8"?>
<layout xmlns:android="http://schemas.android.com/apk/res/android">
   <data>
       <variable name="user" type="com.example.User"/>
   </data>
   <LinearLayout
       android:orientation="vertical"
       android:layout_width="match_parent"
       android:layout_height="match_parent">
       <TextView android:layout_width="wrap_content"
           android:layout_height="wrap_content"
           android:text="@{user.firstName}"/>
       <TextView android:layout_width="wrap_content"
           android:layout_height="wrap_content"
           android:text="@{user.lastName}"/>
   </LinearLayout>
</layout>
```

### モデルクラス (Model)
* レイアウトファイル毎にバインディングクラスが作成される  
    * 対応するレイアウトファイルにバインドしたいプロパティは、このクラスに持たせる
* バインディングクラスは次の命名規則に従って自動生成される
    * レイアウトファイルのパスカル記法 + Binding  
    (e.g. main_activity.xml $\rightarrow$ MainActivityBinding) 
    * ただし、<data\>タグにclass属性を指定するとその名前にできる
    ` <data class="CustomBindingClass">`
* バインドできるモデルのフォーマット
    * JavaBeans (各種privateフィールド+Getter+コンストラクタ)
    * publicフィールド+コンストラクタ  
    ※ Getterは/*(get){フィールド名のキャメルケース}/
* モデルクラスはObservableインタフェースを継承している必要がある
    * 

```
public class User extends BaseObservable {
   private String firstName;
   private String lastName;
   @Bindable
   public String getFirstName() {
       return this.firstName;
   }
   @Bindable
   public String getLastName() {
       return this.lastName;
   }
   public void setFirstName(String firstName) {
       this.firstName = firstName;
       notifyPropertyChanged(BR.firstName);
   }
   public void setLastName(String lastName) {
       this.lastName = lastName;
       notifyPropertyChanged(BR.lastName);
   }
}
```

ビューへのバインドの例
```
@Override
protected void onCreate(Bundle savedInstanceState) {
   super.onCreate(savedInstanceState);
   MainActivityBinding binding = DataBindingUtil.setContentView(this, R.layout.main_activity);
   User user = new User("Test", "User");
   binding.setUser(user);
}
```

RecyclerViewの場合
```
ListItemBinding binding = ListItemBinding.inflate(layoutInflater, viewGroup, false);
```

イベントのバインドの場合も同様です。
```
public class MyHandlers {
    public void onClickFriend(View view) { ... }
    public void onClickEnemy(View view) { ... }
}
```

```
<TextView 
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    android:text="@{user.firstName}"
    android:onClick="@{user.isFriend ? handlers.onClickFriend : handlers.onClickEnemy}"/>
```

## バインド可能なモデルオブジェクトの作成
* バインドするためのモデルクラスは必ずObservableインタフェースを実装したクラス
* 通常はBaseObservableを継承したクラスを実装する。  
    * Getterには@Bindableアノテーションを付ける
    * SetterにはnotifyPropertyChanged(BR.プロパティ名)

```
@Bindable
public String getFirstName() {
  return this.firstName;
}

public void setFirstName(String firstName) {
  this.firstName = firstName;
  notifyPropertyChanged(BR.firstName);
}
```

###ObservableField
上記のようなGetter, Setterの実装が面倒な場合は、ObservableFieldが利用できる。  
上記の例はObservableFieldを用いて以下のように置き換えられる。

```
public final ObservableField<String> firstName = new ObservableField<String>();
```
もしくは
```
public final ObservableString firstName = new ObservableString();
```
この場合、フィールドへのアクセスにはget, setを用いる。

```
user.firstName.set("Seiya");
String name = user.firstName.get(); // "Seiya"
```

###ObservableCollection
Observableなクラスを作る以外にもコレクションを用いたバインドも可能でキーが参照型の時はObservableArrayMapを、int型の時はObservableArrayListを使える。

* ObservableArrayMap
```
ObservableArrayMap<String, Object> user = new ObservableArrayMap<>();
user.put("firstName", "Google");
user.put("lastName", "Inc.");
user.put("age", 17);
```
```
<TextView
   android:text='@{user["lastName"]}'
   android:layout_width="wrap_content"
   android:layout_height="wrap_content"/>
```

* ObservableArrayList
```
ObservableArrayList<Object> user = new ObservableArrayList<>();
user.add("Google");
user.add("Inc.");
user.add(17);
```
```
<TextView
   android:text='@{user[0]}'
   android:layout_width="wrap_content"
   android:layout_height="wrap_content"/>
```

##その他の詳細
###Layoutファイル

* <import\>タグ
    * Javaのimportと同じ  
```
<data>
    <import type="android.view.View"/>
</data>
```

* <include\>タグ
    * Includeした項目にも次のように値バインドする変数を割り当てられる
```
<?xml version="1.0" encoding="utf-8"?>
<layout xmlns:android="http://schemas.android.com/apk/res/android"
        xmlns:bind="http://schemas.android.com/apk/res-auto">
   <data>
       <variable name="user" type="com.example.User"/>
   </data>
   <LinearLayout
       android:orientation="vertical"
       android:layout_width="match_parent"
       android:layout_height="match_parent">
       <include layout="@layout/name"
           bind:user="@{user}"/>
       <include layout="@layout/contact"
           bind:user="@{user}"/>
   </LinearLayout>
</layout>
```

* バインドの指定には式を記述することが可能
```
android:text="@{String.valueOf(index + 1)}"
android:visibility="@{age &lt; 13 ? View.GONE : View.VISIBLE}"
android:transitionName='@{"image_" + id}'
```

##Kotlinでデータバインディングを使う場合
gradleに下記の設定が必要
```
dependencies {
    // ... 略
    kapt 'com.android.databinding:compiler:1.0-rc5'
}

// ... Kotlin Pluginの設定とか

kapt {
    generateStubs = true
}
```

## バインドするデータの型が合っていない場合
Resources.NotFoundExceptionになります。

```
class User {
  public int age;
}

<TextView
  android:text="@{user.age}"/>
```
Resources.NotFoundException


##不具合?
Data Bindingのコンパイラのバージョンを2.1.0-alpha4にするとなぜかビルドが通らなかった。
```
dependencies {
  ...
  kapt 'com.android.databinding:compiler:2.1.0-alpha4'
}
```
```
caused by: java.lang.StringIndexOutOfBoundsException: String index out of range: -1
  at android.databinding.tool.LayoutXmlProcessor.exportLayoutNameFromInfoFileName(LayoutXmlProcessor.java:278)
  at android.databinding.annotationprocessor.ProcessExpressions$IntermediateV2.updateOverridden(ProcessExpressions.java:258)
  at android.databinding.annotationprocessor.ProcessExpressions.onHandleStep(ProcessExpressions.java:75)
  at android.databinding.annotationprocessor.ProcessDataBinding$ProcessingStep.runStep(ProcessDataBinding.java:154)
```

バージョンを1.0-rc5にしたところうまく動作した。
```
dependencies {
  ...
  kapt 'com.android.databinding:compiler:1.0-rc5'
}
```