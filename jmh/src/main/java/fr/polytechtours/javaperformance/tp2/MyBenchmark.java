/*
 * Copyright (c) 2014, Oracle America, Inc.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *  * Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 *
 *  * Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 *
 *  * Neither the name of Oracle nor the names of its contributors may be used
 *    to endorse or promote products derived from this software without
 *    specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF
 * THE POSSIBILITY OF SUCH DAMAGE.
 */

package fr.polytechtours.javaperformance.tp2;

import org.openjdk.jmh.annotations.*;	
import org.openjdk.jmh.infra.Blackhole;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import java.util.concurrent.TimeUnit;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;

// version : 2.0

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

// Implémentation simple
@State(Scope.Thread)
public class MyBenchmark {
	private static final long serialVersionUID = 1L;
	// matrice servant pour le produit de convolution
	static private float[][] mMatriceConv9 = new float[3][3];
	static private float[][] mMatriceConv25 = new float[5][5];
	static private float[][] mMatriceConv49 = new float[7][7];
	// Objet de type Graphics permettant de manipuler l'affichage du Canvas
	private Graphics mGraphics;
	// Objet ne servant que pour les bloc synchronized pour la manipulation du
	// tableau des couleurs
	private Object mMutexCouleurs = new Object();
	// tableau des couleurs, il permert de conserver en memoire l'état de chaque
	// pixel du canvas, ce qui est necessaire au deplacemet des fourmi
	// il sert aussi pour la fonction paint du Canvas
	private Color[][] mCouleurs;
	// couleur du fond
	private Color mCouleurFond = new Color(255, 255, 255);
	// dimensions
	private Dimension mDimension = new Dimension();

	private boolean mSuspendu = false;

	public MyBenchmark(){

	    int i, j;
    		
	    mDimension = new Dimension(5,5);
	    setBounds(new Rectangle(0, 0, mDimension.width, mDimension.height));

	    this.setBackground(mCouleurFond);

	    // initialisation de la matrice des couleurs
	    mCouleurs = new Color[mDimension.width][mDimension.height];
	    synchronized (mMutexCouleurs) {
	      for (i = 0; i != mDimension.width; i++) {
		for (j = 0; j != mDimension.height; j++) {
		  mCouleurs[i][j] = new Color(mCouleurFond.getRed(), mCouleurFond.getGreen(), mCouleurFond.getBlue());
		}
	      }
	    }
	}

	@Benchmark
	public Color test_SetColor() {
		int N = 100;
		Color color = new Color(255,255,255);
		for(int i = 0; i < N; i++){
			setCouleur(1, 1, color, 1);
		}	
		return color;
		
    	}



	public void setCouleur(int x, int y, Color c, int pTaille) {
	    int i, j, k, l, m, n;
	    float R, G, B;
	    Color lColor;

	    synchronized (mMutexCouleurs) {
	      if (!mSuspendu) {
		// on colorie la case sur laquelle se trouve la fourmi
		mGraphics.setColor(c);
		mGraphics.fillRect(x, y, 1, 1);
	      }

	      mCouleurs[x][y] = c;

	      // on fait diffuser la couleur :
		if (pTaille == 1 || pTaille == 2 || pTaille == 3)	      
		{
			short tailleMax = 2*pTaille;

			for (i = 0; i < tailleMax; i++) {
    		for (j = 0; j < tailleMax; j++) {
     			R = G = B = 0f;
					
					for (k = 0; k <= tailleMax; k++) {
        		for (l = 0; l <= tailleMax; l++) {
          		m = (x + i + k - tailleMax + mDimension.width) % mDimension.width;
          		n = (y + j + l - tailleMax + mDimension.height) % mDimension.height;
          		R += mMatriceConv9[k][l] * mCouleurs[m][n].getRed();
							G += mMatriceConv9[k][l] * mCouleurs[m][n].getGreen();
							B += mMatriceConv9[k][l] * mCouleurs[m][n].getBlue();
						}
      		}
      		lColor = new Color((int) R, (int) G, (int) B);

      		mGraphics.setColor(lColor);

      		m = (x + i - pTaille + mDimension.width) % mDimension.width;
      		n = (y + j - pTaille + mDimension.height) % mDimension.height;
      		mCouleurs[m][n] = lColor;
      		if (!mSuspendu) {
        		mGraphics.fillRect(m, n, 1, 1);
      		}
    		}
		  }
		}		
}


