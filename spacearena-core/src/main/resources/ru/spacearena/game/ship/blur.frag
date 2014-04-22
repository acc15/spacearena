precision mediump float;
varying vec2 v_TexCoord;
uniform sampler2D u_Texture;
void main()
{
    vec4 v_Sum = vec4(0,0,0,0);
    for (float y=-1; y<=1; y++) {
        for (float x=-1; x<=1; x++) {
            v_Sum += texture2D(u_Texture, vec2(v_TexCoord.x + x/256.0, v_TexCoord.y + y/256.0));
        }
    }
    gl_FragColor = v_Sum / 3;
}