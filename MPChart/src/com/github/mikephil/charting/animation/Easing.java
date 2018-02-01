// 
// //  adaptation of MPChart
// 

package com.github.mikephil.charting.animation;

public class Easing
{
    public static EasingFunction getEasingFunctionFromOption(final EasingOption easing) {
        switch (easing) {
            default: {
                return EasingFunctions.Linear;
            }
            case EaseInQuad: {
                return EasingFunctions.EaseInQuad;
            }
            case EaseOutQuad: {
                return EasingFunctions.EaseOutQuad;
            }
            case EaseInOutQuad: {
                return EasingFunctions.EaseInOutQuad;
            }
            case EaseInCubic: {
                return EasingFunctions.EaseInCubic;
            }
            case EaseOutCubic: {
                return EasingFunctions.EaseOutCubic;
            }
            case EaseInOutCubic: {
                return EasingFunctions.EaseInOutCubic;
            }
            case EaseInQuart: {
                return EasingFunctions.EaseInQuart;
            }
            case EaseOutQuart: {
                return EasingFunctions.EaseOutQuart;
            }
            case EaseInOutQuart: {
                return EasingFunctions.EaseInOutQuart;
            }
            case EaseInSine: {
                return EasingFunctions.EaseInSine;
            }
            case EaseOutSine: {
                return EasingFunctions.EaseOutSine;
            }
            case EaseInOutSine: {
                return EasingFunctions.EaseInOutSine;
            }
            case EaseInExpo: {
                return EasingFunctions.EaseInExpo;
            }
            case EaseOutExpo: {
                return EasingFunctions.EaseOutExpo;
            }
            case EaseInOutExpo: {
                return EasingFunctions.EaseInOutExpo;
            }
            case EaseInCirc: {
                return EasingFunctions.EaseInCirc;
            }
            case EaseOutCirc: {
                return EasingFunctions.EaseOutCirc;
            }
            case EaseInOutCirc: {
                return EasingFunctions.EaseInOutCirc;
            }
            case EaseInElastic: {
                return EasingFunctions.EaseInElastic;
            }
            case EaseOutElastic: {
                return EasingFunctions.EaseOutElastic;
            }
            case EaseInOutElastic: {
                return EasingFunctions.EaseInOutElastic;
            }
            case EaseInBack: {
                return EasingFunctions.EaseInBack;
            }
            case EaseOutBack: {
                return EasingFunctions.EaseOutBack;
            }
            case EaseInOutBack: {
                return EasingFunctions.EaseInOutBack;
            }
            case EaseInBounce: {
                return EasingFunctions.EaseInBounce;
            }
            case EaseOutBounce: {
                return EasingFunctions.EaseOutBounce;
            }
            case EaseInOutBounce: {
                return EasingFunctions.EaseInOutBounce;
            }
        }
    }
    
    public enum EasingOption
    {
        Linear("Linear", 0), 
        EaseInQuad("EaseInQuad", 1), 
        EaseOutQuad("EaseOutQuad", 2), 
        EaseInOutQuad("EaseInOutQuad", 3), 
        EaseInCubic("EaseInCubic", 4), 
        EaseOutCubic("EaseOutCubic", 5), 
        EaseInOutCubic("EaseInOutCubic", 6), 
        EaseInQuart("EaseInQuart", 7), 
        EaseOutQuart("EaseOutQuart", 8), 
        EaseInOutQuart("EaseInOutQuart", 9), 
        EaseInSine("EaseInSine", 10), 
        EaseOutSine("EaseOutSine", 11), 
        EaseInOutSine("EaseInOutSine", 12), 
        EaseInExpo("EaseInExpo", 13), 
        EaseOutExpo("EaseOutExpo", 14), 
        EaseInOutExpo("EaseInOutExpo", 15), 
        EaseInCirc("EaseInCirc", 16), 
        EaseOutCirc("EaseOutCirc", 17), 
        EaseInOutCirc("EaseInOutCirc", 18), 
        EaseInElastic("EaseInElastic", 19), 
        EaseOutElastic("EaseOutElastic", 20), 
        EaseInOutElastic("EaseInOutElastic", 21), 
        EaseInBack("EaseInBack", 22), 
        EaseOutBack("EaseOutBack", 23), 
        EaseInOutBack("EaseInOutBack", 24), 
        EaseInBounce("EaseInBounce", 25), 
        EaseOutBounce("EaseOutBounce", 26), 
        EaseInOutBounce("EaseInOutBounce", 27);
        
        private EasingOption(final String s, final int n) {
        }
    }
    
    private static class EasingFunctions
    {
        public static final EasingFunction Linear;
        public static final EasingFunction EaseInQuad;
        public static final EasingFunction EaseOutQuad;
        public static final EasingFunction EaseInOutQuad;
        public static final EasingFunction EaseInCubic;
        public static final EasingFunction EaseOutCubic;
        public static final EasingFunction EaseInOutCubic;
        public static final EasingFunction EaseInQuart;
        public static final EasingFunction EaseOutQuart;
        public static final EasingFunction EaseInOutQuart;
        public static final EasingFunction EaseInSine;
        public static final EasingFunction EaseOutSine;
        public static final EasingFunction EaseInOutSine;
        public static final EasingFunction EaseInExpo;
        public static final EasingFunction EaseOutExpo;
        public static final EasingFunction EaseInOutExpo;
        public static final EasingFunction EaseInCirc;
        public static final EasingFunction EaseOutCirc;
        public static final EasingFunction EaseInOutCirc;
        public static final EasingFunction EaseInElastic;
        public static final EasingFunction EaseOutElastic;
        public static final EasingFunction EaseInOutElastic;
        public static final EasingFunction EaseInBack;
        public static final EasingFunction EaseOutBack;
        public static final EasingFunction EaseInOutBack;
        public static final EasingFunction EaseInBounce;
        public static final EasingFunction EaseOutBounce;
        public static final EasingFunction EaseInOutBounce;
        
        static {
            Linear = new EasingFunction() {
                @Override
                public float getInterpolation(final float input) {
                    return input;
                }
            };
            EaseInQuad = new EasingFunction() {
                @Override
                public float getInterpolation(final float input) {
                    return input * input;
                }
            };
            EaseOutQuad = new EasingFunction() {
                @Override
                public float getInterpolation(final float input) {
                    return -input * (input - 2.0f);
                }
            };
            EaseInOutQuad = new EasingFunction() {
                @Override
                public float getInterpolation(final float input) {
                    float position = input / 0.5f;
                    if (position < 1.0f) {
                        return 0.5f * position * position;
                    }
                    return -0.5f * (--position * (position - 2.0f) - 1.0f);
                }
            };
            EaseInCubic = new EasingFunction() {
                @Override
                public float getInterpolation(final float input) {
                    return input * input * input;
                }
            };
            EaseOutCubic = new EasingFunction() {
                @Override
                public float getInterpolation(float input) {
                    --input;
                    return input * input * input + 1.0f;
                }
            };
            EaseInOutCubic = new EasingFunction() {
                @Override
                public float getInterpolation(final float input) {
                    float position = input / 0.5f;
                    if (position < 1.0f) {
                        return 0.5f * position * position * position;
                    }
                    position -= 2.0f;
                    return 0.5f * (position * position * position + 2.0f);
                }
            };
            EaseInQuart = new EasingFunction() {
                @Override
                public float getInterpolation(final float input) {
                    return input * input * input * input;
                }
            };
            EaseOutQuart = new EasingFunction() {
                @Override
                public float getInterpolation(float input) {
                    --input;
                    return -(input * input * input * input - 1.0f);
                }
            };
            EaseInOutQuart = new EasingFunction() {
                @Override
                public float getInterpolation(final float input) {
                    float position = input / 0.5f;
                    if (position < 1.0f) {
                        return 0.5f * position * position * position * position;
                    }
                    position -= 2.0f;
                    return -0.5f * (position * position * position * position - 2.0f);
                }
            };
            EaseInSine = new EasingFunction() {
                @Override
                public float getInterpolation(final float input) {
                    return -(float)Math.cos(input * 1.5707963267948966) + 1.0f;
                }
            };
            EaseOutSine = new EasingFunction() {
                @Override
                public float getInterpolation(final float input) {
                    return (float)Math.sin(input * 1.5707963267948966);
                }
            };
            EaseInOutSine = new EasingFunction() {
                @Override
                public float getInterpolation(final float input) {
                    return -0.5f * ((float)Math.cos(3.141592653589793 * input) - 1.0f);
                }
            };
            EaseInExpo = new EasingFunction() {
                @Override
                public float getInterpolation(final float input) {
                    return (input == 0.0f) ? 0.0f : ((float)Math.pow(2.0, 10.0f * (input - 1.0f)));
                }
            };
            EaseOutExpo = new EasingFunction() {
                @Override
                public float getInterpolation(final float input) {
                    return (input == 1.0f) ? 1.0f : (-(float)Math.pow(2.0, -10.0f * (input + 1.0f)));
                }
            };
            EaseInOutExpo = new EasingFunction() {
                @Override
                public float getInterpolation(final float input) {
                    if (input == 0.0f) {
                        return 0.0f;
                    }
                    if (input == 1.0f) {
                        return 1.0f;
                    }
                    float position = input / 0.5f;
                    if (position < 1.0f) {
                        return 0.5f * (float)Math.pow(2.0, 10.0f * (position - 1.0f));
                    }
                    return 0.5f * (-(float)Math.pow(2.0, -10.0f * --position) + 2.0f);
                }
            };
            EaseInCirc = new EasingFunction() {
                @Override
                public float getInterpolation(final float input) {
                    return -((float)Math.sqrt(1.0f - input * input) - 1.0f);
                }
            };
            EaseOutCirc = new EasingFunction() {
                @Override
                public float getInterpolation(float input) {
                    --input;
                    return (float)Math.sqrt(1.0f - input * input);
                }
            };
            EaseInOutCirc = new EasingFunction() {
                @Override
                public float getInterpolation(final float input) {
                    float position = input / 0.5f;
                    if (position < 1.0f) {
                        return -0.5f * ((float)Math.sqrt(1.0f - position * position) - 1.0f);
                    }
                    return 0.5f * ((float)Math.sqrt(1.0f - (position -= 2.0f) * position) + 1.0f);
                }
            };
            EaseInElastic = new EasingFunction() {
                @Override
                public float getInterpolation(final float input) {
                    if (input == 0.0f) {
                        return 0.0f;
                    }
                    if (input == 1.0f) {
                        return 1.0f;
                    }
                    final float p = 0.3f;
                    final float s = p / 6.2831855f * (float)Math.asin(1.0);
                    final float position;
                    return -((float)Math.pow(2.0, 10.0f * (position = input - 1.0f)) * (float)Math.sin((position - s) * 6.283185307179586 / p));
                }
            };
            EaseOutElastic = new EasingFunction() {
                @Override
                public float getInterpolation(final float input) {
                    if (input == 0.0f) {
                        return 0.0f;
                    }
                    if (input == 1.0f) {
                        return 1.0f;
                    }
                    final float p = 0.3f;
                    final float s = p / 6.2831855f * (float)Math.asin(1.0);
                    return (float)Math.pow(2.0, -10.0f * input) * (float)Math.sin((input - s) * 6.283185307179586 / p) + 1.0f;
                }
            };
            EaseInOutElastic = new EasingFunction() {
                @Override
                public float getInterpolation(final float input) {
                    if (input == 0.0f) {
                        return 0.0f;
                    }
                    float position = input / 0.5f;
                    if (position == 2.0f) {
                        return 1.0f;
                    }
                    final float p = 0.45000002f;
                    final float s = p / 6.2831855f * (float)Math.asin(1.0);
                    if (position < 1.0f) {
                        return -0.5f * ((float)Math.pow(2.0, 10.0f * --position) * (float)Math.sin((position * 1.0f - s) * 6.283185307179586 / p));
                    }
                    return (float)Math.pow(2.0, -10.0f * --position) * (float)Math.sin((position * 1.0f - s) * 6.283185307179586 / p) * 0.5f + 1.0f;
                }
            };
            EaseInBack = new EasingFunction() {
                @Override
                public float getInterpolation(final float input) {
                    final float s = 1.70158f;
                    return input * input * (2.70158f * input - 1.70158f);
                }
            };
            EaseOutBack = new EasingFunction() {
                @Override
                public float getInterpolation(final float input) {
                    final float s = 1.70158f;
                    final float position = input - 1.0f;
                    return position * position * (2.70158f * position + 1.70158f) + 1.0f;
                }
            };
            EaseInOutBack = new EasingFunction() {
                @Override
                public float getInterpolation(final float input) {
                    float s = 1.70158f;
                    float position = input / 0.5f;
                    if (position < 1.0f) {
                        return 0.5f * (position * position * (((s *= 1.525f) + 1.0f) * position - s));
                    }
                    return 0.5f * ((position -= 2.0f) * position * (((s *= 1.525f) + 1.0f) * position + s) + 2.0f);
                }
            };
            EaseInBounce = new EasingFunction() {
                @Override
                public float getInterpolation(final float input) {
                    return 1.0f - EasingFunctions.EaseOutBounce.getInterpolation(1.0f - input);
                }
            };
            EaseOutBounce = new EasingFunction() {
                @Override
                public float getInterpolation(final float input) {
                    if (input < 0.36363637f) {
                        return 7.5625f * input * input;
                    }
                    if (input < 0.72727275f) {
                        final float position;
                        return 7.5625f * (position = input - 0.54545456f) * position + 0.75f;
                    }
                    if (input < 0.90909094f) {
                        final float position;
                        return 7.5625f * (position = input - 0.8181818f) * position + 0.9375f;
                    }
                    float position;
                    return 7.5625f * (position = input - 0.95454544f) * position + 0.984375f;
                }
            };
            EaseInOutBounce = new EasingFunction() {
                @Override
                public float getInterpolation(final float input) {
                    if (input < 0.5f) {
                        return EasingFunctions.EaseInBounce.getInterpolation(input * 2.0f) * 0.5f;
                    }
                    return EasingFunctions.EaseOutBounce.getInterpolation(input * 2.0f - 1.0f) * 0.5f + 0.5f;
                }
            };
        }
    }
}
