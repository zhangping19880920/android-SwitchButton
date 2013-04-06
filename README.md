##Switch Button ##
 
---

Used it in layout xml like:

    <net.yrom.widget.SwitchButton
        android:id="@+id/sb"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
		/>

Declare xml name space like `xmlns:yrom="http://schemas.android.com/apk/res/net.yrom.switchbuttondemo" ` in root that you can use custom attrs.

	<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
		xmlns:yrom="http://schemas.android.com/apk/res/net.yrom.switchbuttondemo"
		android:layout_width="match_parent"
		android:layout_height="match_parent"
		android:orientation="vertical">
		
		...
		
		<net.yrom.widget.SwitchButton
			android:layout_width="wrap_content"
			android:layout_height="wrap_content"
			yrom:switchState="false"
			yrom:background="@drawable/background_switch"
			yrom:btnSlip="@drawable/btn_slip_2"
         />
		 
		</LinearLayout>
        
---
## TODO ##
- make btn Clickable

---
