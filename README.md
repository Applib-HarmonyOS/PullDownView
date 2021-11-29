# PullDownView

A HMOS library which provides pull down and pull up feature to one view over another view

## Source
Inspired by [w4lle/PullDownView](https://github.com/w4lle/PullDownView) - version 1.0.2

## Feature
This library provides pull down and pull up feature to one view over another view.


## Dependency
1. For using pulldownview module in sample app, include the source code and add the below dependencies in entry/build.gradle to generate hap/support.har.
```groovy
	dependencies {
		implementation project(':pulldownlayout')
                implementation fileTree(dir: 'libs', include: ['*.har'])
                testImplementation 'junit:junit:4.13'
	}
```
2. For using pulldownview in separate application using har file, add the har file in the entry/libs folder and add the dependencies in entry/build.gradle file.
```groovy
	dependencies {
		implementation fileTree(dir: 'libs', include: ['*.har'])
		testImplementation 'junit:junit:4.13'
	}
```

## Usage

#### In layout.xml
```xml
<com.w4lle.library.PullDownView
    xmlns:ohos="http://schemas.huawei.com/res/ohos"
    xmlns:custom="http://schemas.huawei.com/res/custom"
    ohos:id="$+id:pull_down_view"
    ohos:height="match_parent"
    ohos:width="match_parent"
    ohos:alignment="center"
    ohos:orientation="vertical"
    custom:is_change_speed="true"
    custom:pull_down_height="30vp"
    custom:pull_up_height="30vp"
    >
```

#### In code

```java
PullDownView mPullDownView = (PullDownView) findComponentById(ResourceTable.Id_pull_down_view);
mPullDownView.setOnPullChangeListerner(new PullDownView.OnPullChangeListerner() {
    @Override
    public void onPullDown() {
        ToastDialog toastDialog = new ToastDialog(getContext());
        toastDialog.setText("onPullDown");
        toastDialog.show();
    }

    @Override
    public void onPullUp() {
        ToastDialog toastDialog = new ToastDialog(getContext());
        toastDialog.setText("onPullUp");
        toastDialog.show();
    }
});
```

## License
```
Copyright (C) 2015 w4lle

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
