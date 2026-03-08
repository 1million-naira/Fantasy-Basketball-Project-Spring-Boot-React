import React from 'react';
import styles from "./custom-styles.module.css";
import { COLORS } from '../constants/COLORS';


function CustomText({children, style={}, as = "span", type='body', color = COLORS.textPrimary}){
    const textColor = type === "button" ? COLORS.buttonText : color;
    const Component = as;

    return (
        <Component className={styles[type]} style={{color : textColor, ...style}}>
            {children}
        </Component>
    )
}

export default CustomText;