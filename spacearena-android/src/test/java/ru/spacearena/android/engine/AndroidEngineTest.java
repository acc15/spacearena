package ru.spacearena.android.engine;

import android.opengl.GLES20;
import org.junit.Test;

import static org.fest.assertions.Assertions.assertThat;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-18-03
 */
public class AndroidEngineTest {

    @Test
    public void testFormat() throws Exception {

        final String s = String.format("Unknown action code: 0x%02x", 1);
        assertThat(s).isEqualTo("Unknown action code: 0x01");

    }

    @Test
    public void testConst() throws Exception {

        System.out.printf("ACTIVE_TEXTURE(0x%04X),%n", GLES20.GL_ACTIVE_TEXTURE);
        System.out.printf("ALIASED_LINE_WIDTH_RANGE(0x%04X),%n", GLES20.GL_ALIASED_LINE_WIDTH_RANGE);
        System.out.printf("ALIASED_POINT_SIZE_RANGE(0x%04X),%n", GLES20.GL_ALIASED_POINT_SIZE_RANGE);
        System.out.printf("ALPHA_BITS(0x%04X),%n", GLES20.GL_ALPHA_BITS);
        System.out.printf("ARRAY_BUFFER_BINDING(0x%04X),%n", GLES20.GL_ARRAY_BUFFER_BINDING);
        System.out.printf("BLEND(0x%04X),%n", GLES20.GL_BLEND);
        System.out.printf("BLEND_COLOR(0x%04X),%n", GLES20.GL_BLEND_COLOR);
        System.out.printf("BLEND_DST_ALPHA(0x%04X),%n", GLES20.GL_BLEND_DST_ALPHA);
        System.out.printf("BLEND_DST_RGB(0x%04X),%n", GLES20.GL_BLEND_DST_RGB);
        System.out.printf("BLEND_EQUATION_ALPHA(0x%04X),%n", GLES20.GL_BLEND_EQUATION_ALPHA);
        System.out.printf("BLEND_EQUATION_RGB(0x%04X),%n", GLES20.GL_BLEND_EQUATION_RGB);
        System.out.printf("BLEND_SRC_ALPHA(0x%04X),%n", GLES20.GL_BLEND_SRC_ALPHA);
        System.out.printf("BLEND_SRC_RGB(0x%04X),%n", GLES20.GL_BLEND_SRC_RGB);
        System.out.printf("BLUE_BITS(0x%04X),%n", GLES20.GL_BLUE_BITS);
        System.out.printf("COLOR_CLEAR_VALUE(0x%04X),%n", GLES20.GL_COLOR_CLEAR_VALUE);
        System.out.printf("COLOR_WRITEMASK(0x%04X),%n", GLES20.GL_COLOR_WRITEMASK);
        System.out.printf("COMPRESSED_TEXTURE_FORMATS(0x%04X),%n", GLES20.GL_COMPRESSED_TEXTURE_FORMATS);
        System.out.printf("CULL_FACE(0x%04X),%n", GLES20.GL_CULL_FACE);
        System.out.printf("CULL_FACE_MODE(0x%04X),%n", GLES20.GL_CULL_FACE_MODE);
        System.out.printf("CURRENT_PROGRAM(0x%04X),%n", GLES20.GL_CURRENT_PROGRAM);
        System.out.printf("DEPTH_BITS(0x%04X),%n", GLES20.GL_DEPTH_BITS);
        System.out.printf("DEPTH_CLEAR_VALUE(0x%04X),%n", GLES20.GL_DEPTH_CLEAR_VALUE);
        System.out.printf("DEPTH_FUNC(0x%04X),%n", GLES20.GL_DEPTH_FUNC);
        System.out.printf("DEPTH_RANGE(0x%04X),%n", GLES20.GL_DEPTH_RANGE);
        System.out.printf("DEPTH_TEST(0x%04X),%n", GLES20.GL_DEPTH_TEST);
        System.out.printf("DEPTH_WRITEMASK(0x%04X),%n", GLES20.GL_DEPTH_WRITEMASK);
        System.out.printf("DITHER(0x%04X),%n", GLES20.GL_DITHER);
        System.out.printf("ELEMENT_ARRAY_BUFFER_BINDING(0x%04X),%n", GLES20.GL_ELEMENT_ARRAY_BUFFER_BINDING);
        System.out.printf("FRAMEBUFFER_BINDING(0x%04X),%n", GLES20.GL_FRAMEBUFFER_BINDING);
        System.out.printf("FRONT_FACE(0x%04X),%n", GLES20.GL_FRONT_FACE);
        System.out.printf("GENERATE_MIPMAP_HINT(0x%04X),%n", GLES20.GL_GENERATE_MIPMAP_HINT);
        System.out.printf("GREEN_BITS(0x%04X),%n", GLES20.GL_GREEN_BITS);
        System.out.printf("IMPLEMENTATION_COLOR_READ_FORMAT(0x%04X),%n", GLES20.GL_IMPLEMENTATION_COLOR_READ_FORMAT);
        System.out.printf("IMPLEMENTATION_COLOR_READ_TYPE(0x%04X),%n", GLES20.GL_IMPLEMENTATION_COLOR_READ_TYPE);
        System.out.printf("LINE_WIDTH(0x%04X),%n", GLES20.GL_LINE_WIDTH);
        System.out.printf("MAX_COMBINED_TEXTURE_IMAGE_UNITS(0x%04X),%n", GLES20.GL_MAX_COMBINED_TEXTURE_IMAGE_UNITS);
        System.out.printf("MAX_CUBE_MAP_TEXTURE_SIZE(0x%04X),%n", GLES20.GL_MAX_CUBE_MAP_TEXTURE_SIZE);
        System.out.printf("MAX_FRAGMENT_UNIFORM_VECTORS(0x%04X),%n", GLES20.GL_MAX_FRAGMENT_UNIFORM_VECTORS);
        System.out.printf("MAX_RENDERBUFFER_SIZE(0x%04X),%n", GLES20.GL_MAX_RENDERBUFFER_SIZE);
        System.out.printf("MAX_TEXTURE_IMAGE_UNITS(0x%04X),%n", GLES20.GL_MAX_TEXTURE_IMAGE_UNITS);
        System.out.printf("MAX_TEXTURE_SIZE(0x%04X),%n", GLES20.GL_MAX_TEXTURE_SIZE);
        System.out.printf("MAX_VARYING_VECTORS(0x%04X),%n", GLES20.GL_MAX_VARYING_VECTORS);
        System.out.printf("MAX_VERTEX_ATTRIBS(0x%04X),%n", GLES20.GL_MAX_VERTEX_ATTRIBS);
        System.out.printf("MAX_VERTEX_TEXTURE_IMAGE_UNITS(0x%04X),%n", GLES20.GL_MAX_VERTEX_TEXTURE_IMAGE_UNITS);
        System.out.printf("MAX_VERTEX_UNIFORM_VECTORS(0x%04X),%n", GLES20.GL_MAX_VERTEX_UNIFORM_VECTORS);
        System.out.printf("MAX_VIEWPORT_DIMS(0x%04X),%n", GLES20.GL_MAX_VIEWPORT_DIMS);
        System.out.printf("NUM_COMPRESSED_TEXTURE_FORMATS(0x%04X),%n", GLES20.GL_NUM_COMPRESSED_TEXTURE_FORMATS);
        System.out.printf("NUM_SHADER_BINARY_FORMATS(0x%04X),%n", GLES20.GL_NUM_SHADER_BINARY_FORMATS);
        System.out.printf("PACK_ALIGNMENT(0x%04X),%n", GLES20.GL_PACK_ALIGNMENT);
        System.out.printf("POLYGON_OFFSET_FACTOR(0x%04X),%n", GLES20.GL_POLYGON_OFFSET_FACTOR);
        System.out.printf("POLYGON_OFFSET_FILL(0x%04X),%n", GLES20.GL_POLYGON_OFFSET_FILL);
        System.out.printf("POLYGON_OFFSET_UNITS(0x%04X),%n", GLES20.GL_POLYGON_OFFSET_UNITS);
        System.out.printf("RED_BITS(0x%04X),%n", GLES20.GL_RED_BITS);
        System.out.printf("RENDERBUFFER_BINDING(0x%04X),%n", GLES20.GL_RENDERBUFFER_BINDING);
        System.out.printf("SAMPLE_ALPHA_TO_COVERAGE(0x%04X),%n", GLES20.GL_SAMPLE_ALPHA_TO_COVERAGE);
        System.out.printf("SAMPLE_BUFFERS(0x%04X),%n", GLES20.GL_SAMPLE_BUFFERS);
        System.out.printf("SAMPLE_COVERAGE(0x%04X),%n", GLES20.GL_SAMPLE_COVERAGE);
        System.out.printf("SAMPLE_COVERAGE_INVERT(0x%04X),%n", GLES20.GL_SAMPLE_COVERAGE_INVERT);
        System.out.printf("SAMPLE_COVERAGE_VALUE(0x%04X),%n", GLES20.GL_SAMPLE_COVERAGE_VALUE);
        System.out.printf("SAMPLES(0x%04X),%n", GLES20.GL_SAMPLES);
        System.out.printf("SCISSOR_BOX(0x%04X),%n", GLES20.GL_SCISSOR_BOX);
        System.out.printf("SCISSOR_TEST(0x%04X),%n", GLES20.GL_SCISSOR_TEST);
        System.out.printf("SHADER_BINARY_FORMATS(0x%04X),%n", GLES20.GL_SHADER_BINARY_FORMATS);
        System.out.printf("SHADER_COMPILER(0x%04X),%n", GLES20.GL_SHADER_COMPILER);
        System.out.printf("STENCIL_BACK_FAIL(0x%04X),%n", GLES20.GL_STENCIL_BACK_FAIL);
        System.out.printf("STENCIL_BACK_FUNC(0x%04X),%n", GLES20.GL_STENCIL_BACK_FUNC);
        System.out.printf("STENCIL_BACK_PASS_DEPTH_FAIL(0x%04X),%n", GLES20.GL_STENCIL_BACK_PASS_DEPTH_FAIL);
        System.out.printf("STENCIL_BACK_PASS_DEPTH_PASS(0x%04X),%n", GLES20.GL_STENCIL_BACK_PASS_DEPTH_PASS);
        System.out.printf("STENCIL_BACK_REF(0x%04X),%n", GLES20.GL_STENCIL_BACK_REF);
        System.out.printf("STENCIL_BACK_VALUE_MASK(0x%04X),%n", GLES20.GL_STENCIL_BACK_VALUE_MASK);
        System.out.printf("STENCIL_BACK_WRITEMASK(0x%04X),%n", GLES20.GL_STENCIL_BACK_WRITEMASK);
        System.out.printf("STENCIL_BITS(0x%04X),%n", GLES20.GL_STENCIL_BITS);
        System.out.printf("STENCIL_CLEAR_VALUE(0x%04X),%n", GLES20.GL_STENCIL_CLEAR_VALUE);
        System.out.printf("STENCIL_FAIL(0x%04X),%n", GLES20.GL_STENCIL_FAIL);
        System.out.printf("STENCIL_FUNC(0x%04X),%n", GLES20.GL_STENCIL_FUNC);
        System.out.printf("STENCIL_PASS_DEPTH_FAIL(0x%04X),%n", GLES20.GL_STENCIL_PASS_DEPTH_FAIL);
        System.out.printf("STENCIL_PASS_DEPTH_PASS(0x%04X),%n", GLES20.GL_STENCIL_PASS_DEPTH_PASS);
        System.out.printf("STENCIL_REF(0x%04X),%n", GLES20.GL_STENCIL_REF);
        System.out.printf("STENCIL_TEST(0x%04X),%n", GLES20.GL_STENCIL_TEST);
        System.out.printf("STENCIL_VALUE_MASK(0x%04X),%n", GLES20.GL_STENCIL_VALUE_MASK);
        System.out.printf("STENCIL_WRITEMASK(0x%04X),%n", GLES20.GL_STENCIL_WRITEMASK);
        System.out.printf("SUBPIXEL_BITS(0x%04X),%n", GLES20.GL_SUBPIXEL_BITS);
        System.out.printf("TEXTURE_BINDING_2D(0x%04X),%n", GLES20.GL_TEXTURE_BINDING_2D);
        System.out.printf("TEXTURE_BINDING_CUBE_MAP(0x%04X),%n", GLES20.GL_TEXTURE_BINDING_CUBE_MAP);
        System.out.printf("UNPACK_ALIGNMENT(0x%04X),%n", GLES20.GL_UNPACK_ALIGNMENT);
        System.out.printf("VIEWPORT(0x%04X);%n", GLES20.GL_VIEWPORT);
        
    }
}
