import React, { useState } from 'react';
import { requireNativeComponent, StyleSheet } from 'react-native';

const DrawFeedComponent = requireNativeComponent('KsDrawFeedAd');

// 关于以前用旧的 native 方式申请的 drawfeed 代码位...
// FIXME: 记得全部重新申请代码位，因为穿山甲马上要弃用旧代码位

export interface DrawFeedAdProps {
    codeid: string;
    visible?: boolean;
    onAdError?: Function;
    onAdShow?: Function;
    onAdClick?: Function;
}

export const KsDrawFeedAd = (props: DrawFeedAdProps) => {
    const { codeid, onAdError, onAdShow, onAdClick, visible = true } = props;
    if (!visible) return null;
    return (
        <DrawFeedComponent
            codeid={codeid}
            style={{ ...styles.container }}
            onAdError={(e: any) => {
                console.log('onAdError DrawFeed', e.nativeEvent);
                onAdError && onAdError(e.nativeEvent);
            }}
            onAdClick={(e: any) => {
                console.log('onAdClick DrawFeed', e.nativeEvent);
                onAdClick && onAdClick(e.nativeEvent);
            }}
            onAdShow={(e: any) => {
                console.log('onAdShow DrawFeed', e.nativeEvent);
                onAdShow && onAdShow(e.nativeEvent);
            }}
        />
    );
};

const styles = StyleSheet.create({
    container: {
        flex: 1,
        width: '100%',
    },
});

export default KsDrawFeedAd;
