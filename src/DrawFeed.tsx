import React, { useState } from "react";
import { requireNativeComponent, StyleSheet } from "react-native";

const DrawFeedComponent = requireNativeComponent("DrawFeedAd");

console.log('DrawFeedComponent', DrawFeedComponent);

// 关于以前用旧的 native 方式申请的 drawfeed 代码位...
// FIXME: 记得全部重新申请代码位，因为穿山甲马上要弃用旧代码位

interface Props {
	appid: string;
	codeid: string;
	onError?: Function;
	onLoad?: Function;
	onClick?: Function;
}

export const DrawFeedAd = (props: Props) => {
	const { appid, codeid, onError, onLoad, onClick } = props;
	const [visible, setVisible] = useState(true);
	return (
		visible && (
			<DrawFeedComponent
				provider={ "头条" }
				appid={ appid }
				codeid={ codeid }
				style={ { ...styles.container } }
				onAdError={ (e: any) => {
					console.log("onError feed", e.nativeEvent);
					setVisible(false);
					onError && onError(e.nativeEvent);
				} }
				onAdClick={ onClick }
				onAdShow={ (e: any) => {
					console.log("onAdShow", e.nativeEvent);
				} }
			/>
		)
	);
};

const styles = StyleSheet.create({
	container: {
		flex: 1,
		width: "100%",
	},
});

export default DrawFeedAd;
