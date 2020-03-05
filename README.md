# appfarms signature dialog 

## Implementation 
1. Insert into your app build.gradle file :
```gradle 
implementation 'com.appfarms:AfSigDialog:VERSION'
```
![Bintray](https://img.shields.io/bintray/v/t-knispel/AfSigDialog/AfSigDialog?label=%20version&style=flat-square)

## Usage


```mermaid
graph LR
A["AfSigDialog(context)"] -- as normal dialog --> B((".show()"))
C --> D("setTrigger(view)")
A -- optional --> C("setTipCount")
A -- optional --> E("setTipTimeOut(time)")
E --> D

```

### Option 1
Use the dialog as normal dialog with the ```show()```function

### Option 2
Use the dialog in "trigger mode" to listen for touches on a specific view