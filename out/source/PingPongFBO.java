import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class PingPongFBO extends PApplet {


// FBO ping pong demo based on the tutorial by Ban the Rewind
// http://www.bantherewind.com/wrap-your-mind-around-your-gpu
// Ported to Processing by Raphaël de Courville <twitter.com/sableRaph>

// see also: http://www.comp.nus.edu/~ashwinna/docs/PingPong_FBO.pdf

/*
* Copyright (c) 2012, Ban the Rewind
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or 
 * without modification, are permitted provided that the following 
 * conditions are met:
 * 
 * Redistributions of source code must retain the above copyright 
 * notice, this list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright 
 * notice, this list of conditions and the following disclaimer in 
 * the documentation and/or other materials provided with the 
 * distribution.
 * 
 * Neither the name of the Ban the Rewind nor the names of its 
 * contributors may be used to endorse or promote products 
 * derived from this software without specific prior written 
 * permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS 
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT 
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS 
 * FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE 
 * COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, 
 * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING,
 * BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; 
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER 
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, 
 * STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) 
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF 
 * ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 * 
 */

PImage image; // Source texture to be refracted

PShader shader;

PGraphics ping, pong;

String shaderFileName = "frag.glsl";
boolean loaded = false;
boolean errorDisplayed;
int reloadEvery = 60;

public void setup() {
  
  noStroke();
  frameRate(60);

  ping = createGraphics(width, height, P2D);
  pong = createGraphics(width, height, P2D);

  image = loadImage("texture.jpg");

  // Set ping and pong to black for the first pass
  ping.beginDraw();
  ping.background(0);
  ping.image(image, 0, 0, width, height); // previous "pong" is passed to the new iteration of the shader
  ping.endDraw();

  pong.beginDraw();
  pong.background(0);
  pong.endDraw();

  tryLoadShader();
  //shader = loadShader("frag.glsl");

  println("setup() finished ok");
}

public void draw() {
    if (frameCount % reloadEvery == 0) {
    tryLoadShader();
  }
    if (!loaded && !errorDisplayed) {
    }
  background(0, 255, 0);

  pong.beginDraw();
  pong.noStroke();
  pong.shader(shader);
  pong.image(ping, 0, 0, width, height); // previous "pong" is passed to the new iteration of the shader
  pong.endDraw();

  image(pong, 0, 0, width, height);

  PGraphics temp = pong;
  pong = ping;
  ping = temp;
}

public void tryLoadShader() {
  try {  
    shader = loadShader( shaderFileName );
    // You have to set at least one uniform here to trigger syntax errors
    shader.set("resolution", PApplet.parseFloat(width), PApplet.parseFloat(height));    
    shader.set("pixel", 1.0f/width, 1.0f/height);
    loaded = true;
    errorDisplayed = false;
  } catch (RuntimeException e) {
    e.printStackTrace();
    loaded = false;
  }
}
  public void settings() {  size(1024, 768, P2D); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "PingPongFBO" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
