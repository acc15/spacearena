precision mediump float;
varying vec2 v_TexCoord;
uniform sampler2D u_Texture;
void main()
{
    vec4 v_Sum = vec4(0.0,0.0,0.0,0.0);
    for (int y=-1; y <= 1; y += 1) {
        for (int x=-1; x <= 1; x += 1) {
            float tx = float(x) / 256.0, ty = float(y) / 256.0;
            vec2 texCoord = vec2(v_TexCoord.x + tx, v_TexCoord.y + ty);
            v_Sum += texture2D(u_Texture, texCoord);
        }
    }
    gl_FragColor = v_Sum / 3.0;
}