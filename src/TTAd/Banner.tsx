import React from "react";
import { requireNativeComponent, StyleSheet } from "react-native";

const NativeBannerAd = requireNativeComponent("TTAdBanner");

interface Props {
  codeId: string;
  // useCache: boolean;
  adWidth: number;
  onError?: Function;
  onLoad?: Function;
  onClick?: Function;
}

const BannerAd = (props: Props) => {
  const { codeId, adWidth = 150, onError, onLoad, onClick } = props;
  let [visible, setVisible] = React.useState(true);
  let [height, setHeight] = React.useState(0); //默认高度
  return (
    visible && (
      <NativeBannerAd
        // provider={banner_provider}
        codeid={codeId}
        adWidth={adWidth}
        style={{ ...styles.container, height }}
        onError={(e: any) => {
          console.log("onError", e.nativeEvent);
          onError && onError(e.nativeEvent);
          setVisible(false);
        }}
        onAdClosed={(e: any) => {
          console.log("onAdClosed", e.nativeEvent);
          setVisible(false);
        }}
        onLayoutChanged={(e: any) => {
          console.log("onLayoutChanged", e.nativeEvent);
          if (e.nativeEvent.height) {
            setHeight(e.nativeEvent.height);
            onLoad && onLoad(e.nativeEvent);
          }
        }}
        onAdClicked={(e: any) => {
          onClick && onClick(e.nativeEvent);
        }}
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

export default BannerAd;
