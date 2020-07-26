import React, { useState } from "react";
import { requireNativeComponent, StyleSheet } from "react-native";

const DrawFeedComponent = requireNativeComponent("DrawFeedAd");

console.log('DrawFeedComponent', DrawFeedComponent);

// 默认很多是用旧的 native 方式申请的 drawfeed 代码位...
// isExpress 用来区分是否用原生方式渲染

interface Props {
	appid: string;
	codeid: string;
	express?: boolean;
	onError?: Function;
	onLoad?: Function;
	onClick?: Function;
}

export const DrawFeedAd = (props: Props) => {
	const { appid, codeid, express, onError, onLoad, onClick } = props;
	const [visible, setVisible] = useState(true);
	return (
		visible && (
			<DrawFeedComponent
				provider={ "头条" }
				express={ express }
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
