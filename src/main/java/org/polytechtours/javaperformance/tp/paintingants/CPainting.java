package org.polytechtours.javaperformance.tp.paintingants;
// package PaintingAnts_v2;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.image.BufferedImage;

// version : 2.0

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * <p>
 * Titre : Painting Ants
 * </p>
 * <p>
 * Description :
 * </p>
 * <p>
 * Copyright : Copyright (c) 2003
 * </p>
 * <p>
 * Société : Equipe Réseaux/TIC - Laboratoire d'Informatique de l'Université de
 * Tours
 * </p>
 *
 * @author Nicolas Monmarché
 * @version 1.0
 */

public class CPainting extends Canvas implements MouseListener {
  private static final long serialVersionUID = 1L;
  // matrice servant pour le produit de convolution
  static private float[][] mMatriceConv9 = new float[3][3];
  static private float[][] mMatriceConv25 = new float[5][5];
  static private float[][] mMatriceConv49 = new float[7][7];
  // Objet de type Graphics permettant de manipuler l'affichage du Canvas
  private Graphics mGraphics;
  // Objet ne servant que pour les bloc synchronized pour la manipulation du
  // tableau des couleurs
  // tableau des couleurs, il permert de conserver en memoire l'état de chaque
  // pixel du canvas, ce qui est necessaire au deplacemewt des fourmi
  // il sert aussi pour la fonction paint du Canvas
  //private Color[][] mCouleurs;
  private int[][] mCouleurs;
  // couleur du fond
  //private Color mCouleurFond = new Color(255, 255, 255);
  // dimensions
  private int mCouleurFond = 65536 * 255 + 256 * 255 + 255;
  
  //Utilisation BuffuredImage
  //private BufferedImage bufferedImage;
  
  private int refresh = 0;
  
  private int height;
  private int width;

  private PaintingAnts mApplis;

  private boolean mSuspendu = false;

  /******************************************************************************
   * Titre : public CPainting() Description : Constructeur de la classe
   ******************************************************************************/
  public CPainting(int h, int w, PaintingAnts pApplis) {
    int i, j;
    addMouseListener(this);

    mApplis = pApplis;

    height = h;
	width = w;
	
	//Utilisation BufferedImage
	//bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
	
    setBounds(new Rectangle(0, 0, width, height));

    this.setBackground(Color.decode(""+mCouleurFond));

    mCouleurs = new int[width][height];

  }

  /******************************************************************************
   * Titre : Color getCouleur Description : Cette fonction renvoie la couleur
   * d'une case
   ******************************************************************************/
  public int getCouleur(int x, int y) {
    ///synchronized (mMutexCouleurs) {
      return mCouleurs[x][y];
    //}
  }

  /******************************************************************************
   * Titre : Color getHauteur Description : Cette fonction renvoie la hauteur de
   * la peinture
   ******************************************************************************/
  public int getHauteur() {
    return height;
  }

  /******************************************************************************
   * Titre : Color getLargeur Description : Cette fonction renvoie la hauteur de
   * la peinture
   ******************************************************************************/
  public int getLargeur() {
    return width;
  }

  /******************************************************************************
   * Titre : void init() Description : Initialise le fond a la couleur blanche
   * et initialise le tableau des couleurs avec la couleur blanche
   ******************************************************************************/
  public void init() {
	
	int i, j;
    mGraphics = getGraphics();
    //synchronized (mMutexCouleurs) {
	mGraphics.clearRect(0, 0, width, height);

	// initialisation de la matrice des couleurs

	for (i = 0; i != width; ++i) {
	for (j = 0; j != height; ++j) {
		mCouleurs[i][j] = 65536 * ((mCouleurFond>>16)&0x0ff) + 256 * ((mCouleurFond>>8)&0x0ff) + ((mCouleurFond)&0x0ff);
	}
	}
    //}

    // initialisation de la matrice de convolution : lissage moyen sur 9
    // cases
    /*
     * 1 2 1 2 4 2 1 2 1
     */
    CPainting.mMatriceConv9[0][0] = 1 / 16f;
    CPainting.mMatriceConv9[0][1] = 2 / 16f;
    CPainting.mMatriceConv9[0][2] = 1 / 16f;
    CPainting.mMatriceConv9[1][0] = 2 / 16f;
    CPainting.mMatriceConv9[1][1] = 4 / 16f;
    CPainting.mMatriceConv9[1][2] = 2 / 16f;
    CPainting.mMatriceConv9[2][0] = 1 / 16f;
    CPainting.mMatriceConv9[2][1] = 2 / 16f;
    CPainting.mMatriceConv9[2][2] = 1 / 16f;

    // initialisation de la matrice de convolution : lissage moyen sur 25
    // cases
    /*
     * 1 1 2 1 1 1 2 3 2 1 2 3 4 3 2 1 2 3 2 1 1 1 2 1 1
     */
    CPainting.mMatriceConv25[0][0] = 1 / 44f;
    CPainting.mMatriceConv25[0][1] = 1 / 44f;
    CPainting.mMatriceConv25[0][2] = 2 / 44f;
    CPainting.mMatriceConv25[0][3] = 1 / 44f;
    CPainting.mMatriceConv25[0][4] = 1 / 44f;
    CPainting.mMatriceConv25[1][0] = 1 / 44f;
    CPainting.mMatriceConv25[1][1] = 2 / 44f;
    CPainting.mMatriceConv25[1][2] = 3 / 44f;
    CPainting.mMatriceConv25[1][3] = 2 / 44f;
    CPainting.mMatriceConv25[1][4] = 1 / 44f;
    CPainting.mMatriceConv25[2][0] = 2 / 44f;
    CPainting.mMatriceConv25[2][1] = 3 / 44f;
    CPainting.mMatriceConv25[2][2] = 4 / 44f;
    CPainting.mMatriceConv25[2][3] = 3 / 44f;
    CPainting.mMatriceConv25[2][4] = 2 / 44f;
    CPainting.mMatriceConv25[3][0] = 1 / 44f;
    CPainting.mMatriceConv25[3][1] = 2 / 44f;
    CPainting.mMatriceConv25[3][2] = 3 / 44f;
    CPainting.mMatriceConv25[3][3] = 2 / 44f;
    CPainting.mMatriceConv25[3][4] = 1 / 44f;
    CPainting.mMatriceConv25[4][0] = 1 / 44f;
    CPainting.mMatriceConv25[4][1] = 1 / 44f;
    CPainting.mMatriceConv25[4][2] = 2 / 44f;
    CPainting.mMatriceConv25[4][3] = 1 / 44f;
    CPainting.mMatriceConv25[4][4] = 1 / 44f;

    // initialisation de la matrice de convolution : lissage moyen sur 49
    // cases
    /*
     * 1 1 2 2 2 1 1 1 2 3 4 3 2 1 2 3 4 5 4 3 2 2 4 5 8 5 4 2 2 3 4 5 4 3 2 1 2
     * 3 4 3 2 1 1 1 2 2 2 1 1
     */
    CPainting.mMatriceConv49[0][0] = 1 / 128f;
    CPainting.mMatriceConv49[0][1] = 1 / 128f;
    CPainting.mMatriceConv49[0][2] = 2 / 128f;
    CPainting.mMatriceConv49[0][3] = 2 / 128f;
    CPainting.mMatriceConv49[0][4] = 2 / 128f;
    CPainting.mMatriceConv49[0][5] = 1 / 128f;
    CPainting.mMatriceConv49[0][6] = 1 / 128f;

    CPainting.mMatriceConv49[1][0] = 1 / 128f;
    CPainting.mMatriceConv49[1][1] = 2 / 128f;
    CPainting.mMatriceConv49[1][2] = 3 / 128f;
    CPainting.mMatriceConv49[1][3] = 4 / 128f;
    CPainting.mMatriceConv49[1][4] = 3 / 128f;
    CPainting.mMatriceConv49[1][5] = 2 / 128f;
    CPainting.mMatriceConv49[1][6] = 1 / 128f;

    CPainting.mMatriceConv49[2][0] = 2 / 128f;
    CPainting.mMatriceConv49[2][1] = 3 / 128f;
    CPainting.mMatriceConv49[2][2] = 4 / 128f;
    CPainting.mMatriceConv49[2][3] = 5 / 128f;
    CPainting.mMatriceConv49[2][4] = 4 / 128f;
    CPainting.mMatriceConv49[2][5] = 3 / 128f;
    CPainting.mMatriceConv49[2][6] = 2 / 128f;

    CPainting.mMatriceConv49[3][0] = 2 / 128f;
    CPainting.mMatriceConv49[3][1] = 4 / 128f;
    CPainting.mMatriceConv49[3][2] = 5 / 128f;
    CPainting.mMatriceConv49[3][3] = 8 / 128f;
    CPainting.mMatriceConv49[3][4] = 5 / 128f;
    CPainting.mMatriceConv49[3][5] = 4 / 128f;
    CPainting.mMatriceConv49[3][6] = 2 / 128f;

    CPainting.mMatriceConv49[4][0] = 2 / 128f;
    CPainting.mMatriceConv49[4][1] = 3 / 128f;
    CPainting.mMatriceConv49[4][2] = 4 / 128f;
    CPainting.mMatriceConv49[4][3] = 5 / 128f;
    CPainting.mMatriceConv49[4][4] = 4 / 128f;
    CPainting.mMatriceConv49[4][5] = 3 / 128f;
    CPainting.mMatriceConv49[4][6] = 2 / 128f;

    CPainting.mMatriceConv49[5][0] = 1 / 128f;
    CPainting.mMatriceConv49[5][1] = 2 / 128f;
    CPainting.mMatriceConv49[5][2] = 3 / 128f;
    CPainting.mMatriceConv49[5][3] = 4 / 128f;
    CPainting.mMatriceConv49[5][4] = 3 / 128f;
    CPainting.mMatriceConv49[5][5] = 2 / 128f;
    CPainting.mMatriceConv49[5][6] = 1 / 128f;

    CPainting.mMatriceConv49[6][0] = 1 / 128f;
    CPainting.mMatriceConv49[6][1] = 1 / 128f;
    CPainting.mMatriceConv49[6][2] = 2 / 128f;
    CPainting.mMatriceConv49[6][3] = 2 / 128f;
    CPainting.mMatriceConv49[6][4] = 2 / 128f;
    CPainting.mMatriceConv49[6][5] = 1 / 128f;
    CPainting.mMatriceConv49[6][6] = 1 / 128f;

    mSuspendu = false;
  }

  /****************************************************************************/
  public void mouseClicked(MouseEvent pMouseEvent) {
    pMouseEvent.consume();
    if (pMouseEvent.getButton() == MouseEvent.BUTTON1) {
      // double clic sur le bouton gauche = effacer et recommencer
      if (pMouseEvent.getClickCount() == 2) {
        init();
      }
      // simple clic = suspendre les calculs et l'affichage
      mApplis.pause();
    } else {
      // bouton du milieu (roulette) = suspendre l'affichage mais
      // continuer les calculs
      if (pMouseEvent.getButton() == MouseEvent.BUTTON2) {
        suspendre();
      } else {
        // clic bouton droit = effacer et recommencer
        // case pMouseEvent.BUTTON3:
        init();
      }
    }
  }

  /****************************************************************************/
  public void mouseEntered(MouseEvent pMouseEvent) {
  }

  /****************************************************************************/
  public void mouseExited(MouseEvent pMouseEvent) {
  }

  /****************************************************************************/
  public void mousePressed(MouseEvent pMouseEvent) {

  }

  /****************************************************************************/
  public void mouseReleased(MouseEvent pMouseEvent) {
  }

  /******************************************************************************
   * Titre : void paint(Graphics g) Description : Surcharge de la fonction qui
   * est appelé lorsque le composant doit être redessiné
   ******************************************************************************/
  @Override
  public void paint(Graphics pGraphics) {
    int i, j;
    
    //Utilisation normale
	  //synchronized (mMutexCouleurs) {
	    for (i = 0; i < width; ++i) {
	      for (j = 0; j < height; ++j) {
	        pGraphics.setColor(Color.decode(""+mCouleurs[i][j]));
	        pGraphics.fillRect(i, j, 1, 1);
	      }
	    }
	  //}
	
  
    //Utilisation bufferedImage
    /*
      for (i = 0; i < width; ++i) {
        for (j = 0; j < height; ++j) {
        	bufferedImage.setRGB(i, j, mCouleurs[i][j]);
        }
      }
	
    pGraphics.drawImage(bufferedImage, 0, 0, null);
	  */
	  
  }

  /******************************************************************************
   * Titre : void colorer_case(int x, int y, int c) Description : Cette
   * fonction va colorer le pixel correspondant et mettre a jour le tableau des
   * couleurs
   ******************************************************************************/
  public void setCouleur(int x, int y, int c, int pTaille) {
	    int i, j, k, l, m, n;
	    float R, G, B;
	    Color lColor;

	    //Utilisation normal
	    //synchronized (mMutexCouleurs) {
	      
	      if (!mSuspendu) {
	        // on colorie la case sur laquelle se trouve la fourmi
	        mGraphics.setColor(Color.decode(""+c));
	        mGraphics.fillRect(x, y, 1, 1);
	      }
	      
	      //Utilisation bufferedImage
	      /*
	      if (!mSuspendu) {
	        // on colorie la case sur laquelle se trouve la fourmi
	    	bufferedImage.setRGB(x, y, mCouleurs[x][y]);
	      }*/

	      mCouleurs[x][y] = c;

	      // on fait diffuser la couleur :
	      if (pTaille == 1 || pTaille == 2 || pTaille == 3)	      
			{
				int tailleMax = 2*pTaille;

				for (i = 0; i <= tailleMax; ++i) {
					for (j = 0; j <= tailleMax; ++j) {
						R = G = B = 0f;
						
						for (k = 0; k <= tailleMax; ++k) {
							for (l = 0; l <= tailleMax; ++l) {
								m = (x + i + k - tailleMax + width) % width;
								n = (y + j + l - tailleMax + height) % height;
								if (pTaille == 1){
									R += mMatriceConv9[k][l] * ((mCouleurs[m][n]>>16)&0x0ff);
									G += mMatriceConv9[k][l] * ((mCouleurs[m][n]>>8)&0x0ff);
									B += mMatriceConv9[k][l] * ((mCouleurs[m][n])&0x0ff);
								}
								else{
									if (pTaille == 2){
										R += mMatriceConv25[k][l] * ((mCouleurs[m][n]>>16)&0x0ff);
										G += mMatriceConv25[k][l] * ((mCouleurs[m][n]>>8)&0x0ff);
										B += mMatriceConv25[k][l] * ((mCouleurs[m][n])&0x0ff);
									}
									else{
										R += mMatriceConv49[k][l] * ((mCouleurs[m][n]>>16)&0x0ff);
										G += mMatriceConv49[k][l] * ((mCouleurs[m][n]>>8)&0x0ff);
										B += mMatriceConv49[k][l] * ((mCouleurs[m][n])&0x0ff);
									}
								}
							}
						}
						
						m = (x + i - pTaille + width) % width;
						n = (y + j - pTaille + height) % height;
						mCouleurs[m][n] = 65536 * (int)R + 256 * (int)G + (int)B;
						
						//Utilisation normal
						lColor = new Color(mCouleurs[m][n]);

						mGraphics.setColor(lColor);
						
						if (!mSuspendu) {
							mGraphics.fillRect(m, n, 1, 1);
						}
						
						//Utilisation du bufferedImage
						/*bufferedImage.setRGB(m, n, mCouleurs[m][n]);
						if (refresh == 10000){
							if (!mSuspendu) {
								mGraphics.drawImage(bufferedImage, 0, 0, null);
							}
							refresh = 0;
						}
						++refresh;*/
					}
				}
			}
	    //}
	  }

  /******************************************************************************
   * Titre : setSupendu Description : Cette fonction change l'état de suspension
   ******************************************************************************/

  public void suspendre() {
    mSuspendu = !mSuspendu;
    if (!mSuspendu) {
      repaint();
    }
  }
}
