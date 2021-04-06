import React from 'react';
import { requireNativeComponent, StyleSheet } from 'react-native';

const FeedAdComponent = requireNativeComponent('FeedAd');
export interface FeedAdProps {
    codeid: string;
    adWidth?: number;
    visible?: boolean;
    onAdLayout?: Function;
    onAdError?: Function;
    onAdClose?: Function;
    onAdClick?: Function;
}

const FeedAd = (props: FeedAdProps) => {
    const { codeid, adWidth = 150, onAdLayout, onAdError, onAdClose, onAdClick, visible = true } = props;
    const [closed, setClosed] = React.useState(false);
    const [height, setHeight] = React.useState(0);
    // FeedAd是否显示，外部和内部均可控制，外部visible、内部closed
    if (!visible || closed) return null;
    return ( 
        <FeedAdComponent
            codeid={codeid}
            adWidth={adWidth - 30}
            style={{ width: adWidth, height }}
            onAdError={(e: any) => {
                onAdError && onAdError(e.nativeEvent);
            }}
            onAdClick={(e: any) => {
                onAdClick && onAdClick(e.nativeEvent);
            }}
            onAdClose={(e: any) => {
                setClosed(true);
                onAdClose && onAdClose(e.nativeEvent);
            }}
            onAdLayout={(e: any) => {
                if (e.nativeEvent.height) {
                    setHeight(e.nativeEvent.height + 10);
                    onAdLayout && onAdLayout(e.nativeEvent);
                }
            }}
        />
    );
};

export default FeedAd;
