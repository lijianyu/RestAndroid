## RestAndroidHttp
基于[okhttp3](http://square.github.io/okhttp/) 简单封装的一个网络请求框架。

## 用法
直接下载源码集成到自己的工程目录下（有时间会上传到jcenter或打包jar包）

## 配置
请在自己定义的application中进行配置，更详细的网络配置参照 `OkHttpClient.Builder`,endPoint
为共用的url块。

``` java
public class YourApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();


        RestAndroidHttp.getInstance().builder(new RestAndroidHttp.Builder()
                .okBuilder(new OkHttpClient.Builder()
                        .addInterceptor(new LoggerInterceptor("", true)))
                .endPoint("https://api.github.com/"));

    }
}

```

## GET 请求

普通请求
``` java
new RestRequest.Builder()
                .urlPath("search/users?q=followers:>10000")
                .build()
                .enqueue(new DataCallback<Result<List<GitUser>>>() {
                    @Override
                    public void onResponse(Result<List<GitUser>> response) {
                        // TODO refresh your view
                    }
                });

```

带url参数的请求,如(username=jack&password=123)
此处只是举例，实际运行没对应的api
``` java
new RestRequest.Builder()
                .urlPath("login")
                .addUrlParams("username","jack")
                .addUrlParams("password","123")
                .build()
                .enqueue(new DataCallback<Result<List<GitUser>>>() {
                    @Override
                    public void onResponse(Result<List<GitUser>> response) {
                        // TODO refresh your view
                    }
                });

```

## POST 请求

表单提交数据
``` java
new RestRequest.Builder()
                .post(new FormRequestBodyFactory()
                .addParam("username","jack")
                .addParam("password","123"))
                .build()
                .enqueue(new DataCallback<String>() {
                    @Override
                    public void onPreExecute() {
                        // show loading dialog
                    }

                    @Override
                    public void onResponse(String response) {
                        // refresh view
                    }

                    @Override
                    public void onError(CallError error) {
                        // refresh view
                    }

                    @Override
                    public void onFinish() {
                        // hide loading dialog
                    }
                });
```

表单提交文件
``` java
new RestRequest.Builder()
                .post(new FormRequestBodyFactory()
                .addFile("file","fileName",new File("")))
                .build()
                .enqueue(new DataCallback<String>() {
                    @Override
                    public void onPreExecute() {
                        // show loading dialog
                    }

                    @Override
                    public void onProgress(float progress, float length) {
                        // refresh view
                    }

                    @Override
                    public void onResponse(String response) {
                        // refresh view
                    }

                    @Override
                    public void onError(CallError error) {
                        // refresh view
                    }

                    @Override
                    public void onFinish() {
                        // hide loading dialog
                    }
                });

```

以json格式传递参数
``` java
new RestRequest.Builder()
                .post(new JsonRequestBodyFactory(jsonObject))
                .build()
                .enqueue(new DataCallback<String>() {
                    @Override
                    public void onPreExecute() {
                        // show loading dialog
                    }

                    @Override
                    public void onProgress(float progress, float length) {
                        // refresh view
                    }

                    @Override
                    public void onResponse(String response) {
                        // refresh view
                    }

                    @Override
                    public void onError(CallError error) {
                        // refresh view
                    }

                    @Override
                    public void onFinish() {
                        // hide loading dialog
                    }
                });

```

自定义参数构造工厂
``` java
new RestRequest.Builder()
               .post(new RequestBodyFactory() {
                   @Override
                   public RequestBody buildRequestBody() {
                       return null;
                   }
               })
               .build()
               .enqueue(new DataCallback<String>() {
                   @Override
                   public void onPreExecute() {
                       // show loading dialog
                   }

                   @Override
                   public void onProgress(float progress, float length) {
                       // refresh view
                   }

                   @Override
                   public void onResponse(String response) {
                       // refresh view
                   }

                   @Override
                   public void onError(CallError error) {
                       // refresh view
                   }

                   @Override
                   public void onFinish() {
                       // hide loading dialog
                   }
               });


```

## callback

目前内部包含DataCallback,FileCallback，可以根据自己的需求去自定义Callback，例如希望回调User对象：

默认使用DataCallback回调，内部以Gosn实现数据转换，如：

``` java
new RestRequest.Builder()
                .urlPath("login")
                .addUrlParams("username","jack")
                .addUrlParams("password","123")
                .build()
                .enqueue(new DataCallback<User>() {
                    @Override
                    public void onResponse(User response) {
                        // TODO refresh your view
                    }
                });

```

也可自己定义
``` java
public abstract class UserCallback extends Callback<User>
{
    @Override
    public User parseNetworkResponse(Response response) throws IOException
    {
        String string = response.body().string();
        User user = new Gson().fromJson(string, User.class);
        return user;
    }
}


```

## 补充
>* 此框架主要针对参数跟返回数据以及超时设置的一个封装，更多的操作，如cookie，访问拦截，https等参照okhttp使用。
另外具体的图片（建议使用像glide这种比较好的第三方库），https设置等暂时没有添加，有需要的可以自己进一步封装。

>* 此框架有参考[鸿洋大神的okHttpUtil](https://github.com/hongyangAndroid/okhttp-utils),特别感谢.

[my blog](http://lijianyu.github.io/)

[转发请注明出处，非常感谢]
