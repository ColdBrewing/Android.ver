# Android
## APIKEY 변경법
실행을 위해서는 먼저 Cloud Vision API키를 넣어야 함.

### apikey 삽입위치
1. class BuildingResult 62줄
private static final String CLOUD_VISION_API_KEY = "apikey"

"apikey"를 "API키"바꿀 것.

2. Gradle Scripts-build.gradle(Module:app) 19줄
buildTypes-buildTypes.each에서
it.buildConfigField('String', 'API_KEY', apikey)

apikey를 '"API키"'로 바꿀 것.


##지도 API key 변경
실행을 위해서는 먼저 KAKAO MAP API키를 넣어야 함.

### apikey 삽입위치
1. AndroidManifest.xml 39줄
<meta-data android:name="com.kakao.sdk.AppKey" android:value="XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX"/>

2. findLoad.java 40줄
mapView.setDaumMapApiKey("XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");

"XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX"자리에 "API키" 넣기