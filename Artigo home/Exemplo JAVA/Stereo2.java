import java.applet.*;
import java.awt.*;
import java.util.Vector;
import Stereo2Frame;

public class Stereo2 extends Applet
{
	private boolean m_fStandAlone = false;

	public static void main(String args[])
	{
		Stereo2Frame frame = new Stereo2Frame("Stereo2");

		frame.show();
        frame.hide();
		frame.resize(frame.insets().left + frame.insets().right  + 700,
					 frame.insets().top  + frame.insets().bottom + 500);

		Stereo2 applet_Stereo2 = new Stereo2();

		frame.add("Center", applet_Stereo2);
		applet_Stereo2.m_fStandAlone = true;
		applet_Stereo2.init();
		applet_Stereo2.start();
        frame.show();
	}

	public Stereo2()
	{
	}

	public String getAppletInfo()
	{
		return "Name: Stereo Vision\r\n" +
		       "Author: Guillaume STAMM\r\n" +
		       "308-644 Patern Recognition 1998";
	}


	public void init()
	{
    	resize(700, 500);
		setLayout(new BorderLayout());
		Kernel k = new Kernel();
        Draw_canvas cl = new Draw_canvas(k);
		Draw_canvas cr = new Draw_canvas(k);
		Draw_canvas cc = new Draw_canvas(k);
		
		// place the canavas in a layout
		GridBagLayout  gridbag  =  new  GridBagLayout();
		GridBagConstraints  c  =  new  GridBagConstraints();
		Panel display_panel;
		display_panel = new Panel();
		display_panel.setLayout(gridbag);
		c.fill  =  GridBagConstraints.BOTH;
		c.weightx  =  1.0;c.weighty  =  1.0;
		c.gridx = 0;c.gridy = 0;
		gridbag.setConstraints(cl, c);               
		display_panel.add(cl);
		c.gridx = 1;
		gridbag.setConstraints(cr, c);               
		display_panel.add(cr);
		c.weightx  =  2.0;c.weighty  =  2.0;
		c.gridx = 0;c.gridy = 1;
		c.gridwidth = 2;
		gridbag.setConstraints(cc, c);               
		display_panel.add(cc);
		
		Control_panel controls = new Control_panel(k);
		add("Center", display_panel);
        add("East", controls);
		k.construc(cl,cr,cc,display_panel,controls);

	}

	public void destroy()
	{
	}

	public void paint(Graphics g)
	{
	}

	public void start()
	{
	}
	
	public void stop()
	{
	}

}

class Kernel
{
	Draw_canvas dl, dr, dc;
	boolean have_to_draw;
	Panel display_p;
	boolean cross_eyes;
	boolean pt; // projection type

	Control_panel controls;
	Vector points = new Vector(10,5);
	Point edit_p, mark_p, del_a, del_b;
	boolean mark, edit;
	
	// Point of view
	int lat, lat_speed, delta_lat, lon, lon_speed, delta_lon;
	float dis, dis_proj, eyes, sca;
	
	Vecteur vi_l,vj_l,vn_l,vi_r,vj_r,vn_r;
	fPoint center;
	fPoint left_eye, right_eye;

	Animation anim;
	boolean anim_run = false;
	
	public Kernel() 
	{
		initialisation();
	}
	
	public void initialisation() 
	{
		cross_eyes = false;
		pt = true;
		mark = false;
		edit = true;
		edit_p = new Point(0,0,0);
		mark_p = new Point(0,0,0);
		del_a = new Point(0,0,0);
		del_b = new Point(0,0,0);
		//points = null;
		vi_l = new Vecteur(0,0,0);vj_l = new Vecteur(0,0,0);
		vn_l = new Vecteur(0,0,0);
		vi_r = new Vecteur(0,0,0);vj_r = new Vecteur(0,0,0);
		vn_r = new Vecteur(0,0,0);
		center = new fPoint(0,0,0);
		left_eye = new fPoint(0,0,0); right_eye = new fPoint(0,0,0);
		lat = 0; lat_speed = 0; delta_lat = 2;
		lon = 0; lon_speed = 0; delta_lon = 2;
		//alpha = 0; delta_alpha = 0;
		//beta = 0; delta_beta = 0;
		dis = 10; dis_proj = 1; eyes = 1; sca = 500;
		
		compute_eyes_position();
	}
	
	public void ev_reset()
	{
		initialisation();
		points = new Vector(10,5);
		edit_p.x_min = 10000;
		edit_p.x_max = -10000;
		edit_p.y_min = 10000;
		edit_p.y_max = -10000; 
		edit_p.z_min = 10000;
		edit_p.z_max = -10000; 
		ev_stop_move();
		paint_request();
	}
	public void construc(Draw_canvas cl, Draw_canvas cr, Draw_canvas cc, Panel d, Control_panel c)
	{
		dl = cl; dr = cr; dc = cc;
		controls = c;
		display_p = d;
		recalculate();

		/* Drawing of a small house as demo */
		//lat_speed = 1; lon_speed = 2;
		sca = 200;
		ev_change_val(true);
		mark_p.x = -2; mark_p.y = -3; mark_p.z = -2;
		edit_p.x = 2; edit_p.y = -3; edit_p.z = -2;
		ev_draw_seg(); ev_mark();
		controls.draw_and_mark_b.enable();
		controls.del_b.enable();
		ev_change_val(true);
		edit_p.y += 6; ev_draw_seg(); ev_mark(); //ev_change_val(true);
		edit_p.x -= 4; ev_draw_seg(); ev_mark(); //ev_change_val(true);
		edit_p.y -= 6; ev_draw_seg(); ev_mark(); //ev_change_val(true);
		edit_p.z += 4; ev_draw_seg(); ev_mark(); //ev_change_val(true);
		edit_p.x += 4; ev_draw_seg(); ev_mark(); //ev_change_val(true);
		edit_p.y += 6; ev_draw_seg(); ev_mark(); //ev_change_val(true);
		edit_p.x -= 4; ev_draw_seg(); ev_mark(); //ev_change_val(true);
		edit_p.y -= 6; ev_draw_seg(); ev_mark(); //ev_change_val(true);
		edit_p.x += 2; edit_p.y += 3; edit_p.z += 2; ev_draw_seg(); ev_mark(); //ev_change_val(true);
		edit_p.x += 2; edit_p.y += 3; edit_p.z -= 2; ev_draw_seg(); ev_mark(); //ev_change_val(true);
		edit_p.z -= 4; ev_draw_seg(); ev_mark(); //ev_change_val(true);
		edit_p.y -= 6; ev_draw_seg(); ev_mark(); //ev_change_val(true);
		edit_p.z += 4; ev_draw_seg(); ev_mark(); //ev_change_val(true);
		edit_p.x -= 2; edit_p.y += 3; edit_p.z += 2; ev_draw_seg(); ev_mark(); //ev_change_val(true);
		edit_p.x -= 2; edit_p.y += 3; edit_p.z -= 2; ev_draw_seg(); ev_mark(); //ev_change_val(true);
		edit_p.z -= 4; ev_draw_seg(); ev_mark(); 
		ev_change_val(true);
		//ev_move();
	}
	
	public void ev_mark()
	{
		mark = true;
		mark_p.copy(edit_p);
		paint_request();
	}
	
	public void ev_draw_seg()
	{
		int mark_in = -1;
		int edit_in = -1;
		Point p_tmp;
		
		del_a.copy(mark_p);
		del_b.copy(edit_p);
		if (points.size() == 0)
		{
			points.addElement(new Point(mark_p));
			mark_in = 0;
		}
		else
		{
			for(int i=0; i< points.size(); i++)
			{
				p_tmp = (Point)points.elementAt(i);
				if (mark_p.equal(p_tmp))
					mark_in = i;
				if (p_tmp.equal(edit_p))
					edit_in = i;
			}
		}
		if (mark_in == -1)
		{
			points.addElement(new Point(mark_p));
			mark_in = points.size()-1;
		}
		if (edit_in == -1)
		{
			points.addElement(new Point(edit_p));
			edit_in = points.size()-1;
		}
		((Point)points.elementAt((edit_in < mark_in) ? edit_in : mark_in)).add_list_point((edit_in < mark_in) ? mark_in : edit_in);
		
		mark = false;
		p_tmp = (Point)points.elementAt(0);
		center.x = (float)(p_tmp.x_max + p_tmp.x_min)/2;
		center.y = (float)(p_tmp.y_max + p_tmp.y_min)/2;
		center.z = (float)(p_tmp.z_max + p_tmp.z_min)/2;
		recalculate();
	}
	
	public boolean delete_seg(Point a, Point b)
	{
		int a_in = -1;
		int b_in = -1;
		Point p_tmp;
		
		for(int i=0; i< points.size(); i++)
		{
			p_tmp = (Point)points.elementAt(i);
			if (a.equal(p_tmp))
				a_in = i;
			if (p_tmp.equal(b))
				b_in = i;
		}
		
		if ((a_in != -1) && (b_in != -1) && (a_in != b_in) &&
			((Point)points.elementAt((a_in < b_in) ? a_in : b_in)).remove_list_point((a_in < b_in) ? b_in : a_in)) {
			paint_request();return true;}
		return false;
	}
	
	public void compute_eyes_position()
	{
		right_eye.z = dis*(float)Math.sin(lat*Math.PI/180);
		left_eye.z = right_eye.z;
		float tmp = dis*(float)Math.cos(lat*Math.PI/180);
		float cl = (float)Math.cos(lon*Math.PI/180);
		float sl = (float)Math.sin(lon*Math.PI/180);
		left_eye.x = cl*tmp + sl*eyes;
		right_eye.x = cl*tmp - sl*eyes;
		left_eye.y = sl*tmp - cl*eyes;
		right_eye.y = sl*tmp + cl*eyes;
	}
	
	public void compute_n()
	{
		vn_l.set(center,left_eye);
		vn_l.normalize(1,0,0);
		vn_r.set(center,right_eye);
		vn_r.normalize(1,0,0);
	}
	
	public void compute_ij()
	{
		vi_l.set(left_eye,right_eye);
		vj_l.cross(vn_l,vi_l);
		vj_l.normalize(0,1,0);
		vi_l.cross(vj_l,vn_l);
		vi_l.normalize(0,0,1);
		
		vi_r.set(left_eye,right_eye);
		vj_r.cross(vn_r,vi_r);
		vj_r.normalize(0,1,0);
		vi_r.cross(vj_r,vn_r);
		vi_r.normalize(0,0,1);
	}
	
	public void ev_change_val(boolean all)
	{
		if (lat>180) lat -= 360;
		if (lat<-180) lat += 360;
		if (lon>180) lon -= 360;
		if (lon<-180) lon += 360;
		if (all) controls.refrech();
		else controls.anim_refrech();
		recalculate();
	}
	
	public void recalculate()
	{
		compute_eyes_position();
		compute_n();
		compute_ij();
		paint_request();
	}
	
	public void paint_request() 
	{
		have_to_draw = true;
		if (anim_run) {
			dr.paint();
			dl.paint();
			dc.paint();
		}
		else {
			dr.repaint();
			dl.repaint();
			dc.repaint();
		}
	}
	
	public void ev_can_paint() 
	{
		Graphics l,r,c;
		int txbl, txbr, tybl, tybr, tx, ty, tdx, tdy, dlh, dlw, drh, drw, dch, dcw; // temporary variable to win some time
		Vecteur vl, vl2, vr, vr2, vtemp1, vtemp2;
		float temp1, temp2;
		Point tmp1, tmp2;
		List_point lp;
		
		if (have_to_draw) {
			have_to_draw = false;
			dl.ResizeImage();
			dr.ResizeImage();
			dc.ResizeImage();
			l = dl.m_g; r = dr.m_g; c = dc.m_g; 
			dlh = dl.m_dimImage.height; dlw = dl.m_dimImage.width; 
			drh = dr.m_dimImage.height; drw = dr.m_dimImage.width; 
			dch = dc.m_dimImage.height; dcw = dc.m_dimImage.width; 
			
			/* rectangle around the window */
			l.setColor(Color.black);r.setColor(Color.black);c.setColor(Color.black);
			l.drawLine(0, 0, dlw-1, 0);r.drawLine(0, 0, drw-1, 0);c.drawLine(0, 0, dcw-1, 0);
			l.drawLine(0, 0, 0, dlh-1);r.drawLine(0, 0, 0, drh-1);c.drawLine(0, 0, 0, dch-1);
			
			l.setColor(Color.white);r.setColor(Color.white);c.setColor(Color.white);
			l.drawLine(dlw-1, 0, dlw-1, dlh-1);r.drawLine(drw-1, 0, drw-1, drh-1);c.drawLine(dcw-1, 0, dcw-1, dch-1);
			l.drawLine(0, dlh-1, dlw-1, dlh-1);r.drawLine(0, drh-1, drw-1, drh-1);c.drawLine(0, dch-1, dcw-1, dch-1);
			
			l.setColor(Color.gray);r.setColor(Color.gray);c.setColor(Color.black);
			l.fillRect(1,1,dlw-2,dlh-2);r.fillRect(1,1,drw-2,drh-2);c.fillRect(1,1,dcw-2,dch-2);
			
			dlh /= 2; dlw /= 2; drh /= 2; drw /= 2; dch /= 2; dcw /= 2; 
			
			vl = new Vecteur(left_eye,edit_p);
			vr = new Vecteur(right_eye,edit_p);
			vl2 = new Vecteur(left_eye,edit_p);
			vr2 = new Vecteur(right_eye,edit_p);
			vtemp1 = new Vecteur(0,0,0);
			vtemp2 = new Vecteur(0,0,0);
			
			// Draw the edit point
			l.setColor(Color.red);r.setColor(Color.red);c.setColor(Color.red);
			if (pt) {
				temp1 = -vn_l.scal(vl);
				if (temp1 > dis_proj) {
					temp1 = dis_proj/temp1;
					vtemp1.set(vl.dx*temp1, vl.dy*temp1, vl.dz*temp1);
					tx = (int)(sca*vi_l.scal(vtemp1))-2; ty = (int)(sca*vj_l.scal(vtemp1))-2; 
					(cross_eyes ? r :l).fillOval(tx+dlw, ty+dlh, 5, 5); c.fillOval(tx+dcw, ty+dch, 5, 5);
				}
			}
			else {
				tx = (int)(sca*vi_l.scal(vl))-2; ty = (int)(sca*vj_l.scal(vl))-2;
				(cross_eyes ? r :l).fillOval(tx+dlw, ty+dlh, 5, 5); c.fillOval(tx+dcw, ty+dch, 5, 5);
			}
			c.setColor(Color.blue);
			if (pt) {
				temp1 = -vn_r.scal(vr);
				if (temp1 > dis_proj) {
					temp1 = dis_proj/temp1;
					vtemp1.set(vr.dx*temp1, vr.dy*temp1, vr.dz*temp1);
					tx = (int)(sca*vi_r.scal(vtemp1))-2; ty = (int)(sca*vj_r.scal(vtemp1))-2; 
					(cross_eyes ? l :r).fillOval(tx+drw, ty+drh, 5, 5); c.fillOval(tx+dcw, ty+dch, 5, 5);
				}
			}
			else {
				tx = (int)(sca*vi_r.scal(vr))-2; ty = (int)(sca*vj_r.scal(vr))-2;
				(cross_eyes ? l : r).fillOval(tx+drw, ty+drh, 5, 5); c.fillOval(tx+dcw, ty+dch, 5, 5);
			}
			// Draw the mark point
			if (mark)
			{
				vl.set(left_eye,mark_p); vr.set(right_eye,mark_p);
				l.setColor(Color.green);r.setColor(Color.green);c.setColor(Color.green);
				
				if (pt) {
					temp1 = -vn_l.scal(vl);
					if (temp1 > dis_proj) {
						temp1 = dis_proj/temp1;
						vtemp1.set(vl.dx*temp1, vl.dy*temp1, vl.dz*temp1);
						tx = (int)(sca*vi_l.scal(vtemp1))-2; ty = (int)(sca*vj_l.scal(vtemp1))-2; 
						(cross_eyes ? r :l).fillOval(tx+dlw, ty+dlh, 5, 5); c.fillOval(tx+dcw, ty+dch, 5, 5);
					}
				}
				else {
					tx = (int)(sca*vi_l.scal(vl))-2; ty = (int)(sca*vj_l.scal(vl))-2;
					(cross_eyes ? r :l).fillOval(tx+dlw, ty+dlh, 5, 5); c.fillOval(tx+dcw, ty+dch, 5, 5);
				}			
				if (pt) {
					temp1 = -vn_r.scal(vr);
					if (temp1 > dis_proj) {
						temp1 = dis_proj/temp1;
						vtemp1.set(vr.dx*temp1, vr.dy*temp1, vr.dz*temp1);
						tx = (int)(sca*vi_r.scal(vtemp1))-2; ty = (int)(sca*vj_r.scal(vtemp1))-2; 
						(cross_eyes ? l : r).fillOval(tx+drw, ty+drh, 5, 5); c.fillOval(tx+dcw, ty+dch, 5, 5);
					}
				}
				else {
					tx = (int)(sca*vi_r.scal(vr))-2; ty = (int)(sca*vj_r.scal(vr))-2;
					(cross_eyes ? l : r).fillOval(tx+drw, ty+drh, 5, 5); c.fillOval(tx+dcw, ty+dch, 5, 5);
				}
			}
			
			// draw all the segments
			l.setColor(Color.white);r.setColor(Color.white);
			for (int i=0; i<points.size() - 1; i++)
			{
				tmp1 = (Point)points.elementAt(i);
				vl.set(left_eye,tmp1); vr.set(right_eye,tmp1);
				if (pt) {
					temp1 = -vn_l.scal(vl);
					if (temp1 > dis_proj) {
						temp1 = dis_proj/temp1;
						vtemp1.set(vl.dx*temp1, vl.dy*temp1, vl.dz*temp1);
						txbl = (int)(sca*vi_l.scal(vtemp1)); tybl = (int)(sca*vj_l.scal(vtemp1)); 
						
						lp = tmp1.l_p;
						while (lp != null)
						{
							tmp2 = (Point)points.elementAt(lp.point_index);
							vl2.set(left_eye,tmp2);
							
							temp2 = -vn_l.scal(vl2);
							if (temp2 > dis_proj) {
								temp2 = dis_proj/temp2;
								vtemp2.set(vl2.dx*temp2, vl2.dy*temp2, vl2.dz*temp2);
								
								c.setColor(new Color(180,0,0));
								tdx = (int)(sca*vi_l.scal(vtemp2)); tdy = (int)(sca*vj_l.scal(vtemp2)); 
								(cross_eyes ? r :l).drawLine(txbl+dlw, tybl+dlh, tdx+dlw, tdy+dlh); c.drawLine(txbl+dcw, tybl+dch, tdx+dcw, tdy+dch);
							}
							lp = lp.next;
						}
					}
					
					temp1 = -vn_r.scal(vr);
					if (temp1 > dis_proj) {
						temp1 = dis_proj/temp1;
						vtemp1.set(vr.dx*temp1, vr.dy*temp1, vr.dz*temp1);
						txbr = (int)(sca*vi_r.scal(vtemp1)); tybr = (int)(sca*vj_r.scal(vtemp1)); 
						
						lp = tmp1.l_p;
						while (lp != null)
						{
							tmp2 = (Point)points.elementAt(lp.point_index);
							vr2.set(right_eye,tmp2);
							
							temp2 = -vn_r.scal(vr2);
							if (temp2 > dis_proj) {
								temp2 = dis_proj/temp2;
								vtemp2.set(vr2.dx*temp2, vr2.dy*temp2, vr2.dz*temp2);
								
								c.setColor(new Color(0,0,255));
								tdx = (int)(sca*vi_r.scal(vtemp2)); tdy = (int)(sca*vj_r.scal(vtemp2)); 
								(cross_eyes ? l : r).drawLine(txbr+drw, tybr+drh, tdx+drw, tdy+drh); c.drawLine(txbr+dcw, tybr+dch, tdx+dcw, tdy+dch);
							}
							lp = lp.next;
						}
					}
				}
				else {
					txbl = (int)(sca*vi_l.scal(vl)); tybl = (int)(sca*vj_l.scal(vl));
					txbr = (int)(sca*vi_r.scal(vr)); tybr = (int)(sca*vj_r.scal(vr));
					
					lp = tmp1.l_p;
					while (lp != null)
					{
						tmp2 = (Point)points.elementAt(lp.point_index);
						vl2.set(left_eye,tmp2);vr2.set(right_eye,tmp2);
						
						c.setColor(new Color(180,0,0));
						tdx = (int)(sca*vi_l.scal(vl2)); tdy = (int)(sca*vj_l.scal(vl2));
						(cross_eyes ? r :l).drawLine(txbl+dlw, tybl+dlh, tdx+dlw, tdy+dlh); c.drawLine(txbl+dcw, tybl+dch, tdx+dcw, tdy+dch);
						
						c.setColor(new Color(0,0,255));
						tdx = (int)(sca*vi_r.scal(vr2)); tdy = (int)(sca*vj_r.scal(vr2));
						(cross_eyes ? l : r).drawLine(txbr+drw, tybr+drh, tdx+drw, tdy+drh); c.drawLine(txbr+dcw, tybr+dch, tdx+dcw, tdy+dch);
						
						lp = lp.next;
					}
				}
			}
		}	
	}

	public void ev_can_click(Event ev, Draw_canvas can) 
	{
		int center_x, center_y;
		float pos_x, pos_y, tmp;
		Vecteur I,J,N;
		fPoint eye;
		
		I = vi_l; J = vj_l; N = vn_l;
		eye = left_eye;
		center_y = dl.m_dimImage.height/2;
		center_x = dl.m_dimImage.width/2; 
		if (((can == dl) && (!cross_eyes)) || ((can == dr) && (cross_eyes))) {
			center_y = dl.m_dimImage.height/2;
			center_x = dl.m_dimImage.width/2; 
			I = vi_l; J = vj_l; N = vn_l;
			eye = left_eye;
		}
		if (((can == dl) && (cross_eyes)) || ((can == dr) && (!cross_eyes))) {
			center_y = dr.m_dimImage.height/2;
			center_x = dr.m_dimImage.width/2; 
			I = vi_r; J = vj_r; N = vn_r;
			eye = right_eye;
		}
		if (can == dc) {
			center_y = dc.m_dimImage.height/2;
			center_x = dc.m_dimImage.width/2; 
			I = new Vecteur((vi_l.dx+vi_r.dx)/2, (vi_l.dy+vi_r.dy)/2, (vi_l.dz+vi_r.dz)/2);
			J = new Vecteur((vj_l.dx+vj_r.dx)/2, (vj_l.dy+vj_r.dy)/2, (vj_l.dz+vj_r.dz)/2);
			N = new Vecteur((vn_l.dx+vn_r.dx)/2, (vn_l.dy+vn_r.dy)/2, (vn_l.dz+vn_r.dz)/2);
			eye = new fPoint((left_eye.x+right_eye.x)/2, (left_eye.y+right_eye.y)/2, (left_eye.z+right_eye.z)/2);
		}
		pos_x = (ev.x-center_x)/sca; 
		pos_y = (ev.y-center_y)/sca;
		
		if (pt) {
			tmp = N.scal(new Vecteur(center, eye))/dis_proj;
			edit_p.x = Math.round((-dis_proj*N.dx + pos_x*I.dx + pos_y*J.dx)*tmp+eye.x);
			edit_p.y = Math.round((-dis_proj*N.dy + pos_x*I.dy + pos_y*J.dy)*tmp+eye.y);
			edit_p.z = Math.round((-dis_proj*N.dz + pos_x*I.dz + pos_y*J.dz)*tmp+eye.z);
		}
		else {
			edit_p.x = Math.round(center.x + pos_x*I.dx + pos_y*J.dx);
			edit_p.y = Math.round(center.y + pos_x*I.dy + pos_y*J.dy);
			edit_p.z = Math.round(center.z + pos_x*I.dz + pos_y*J.dz);
		}
		ev_change_val(true);
	}

	public void ev_move() 
	{
		if (!anim_run) {
			anim = new Animation(this);
			anim.start();
			anim_run = true;
		}
	}
	
	public void ev_stop_move() 
	{
		if (anim_run) anim.stop();
		anim_run = false;
	}	
}

class Draw_canvas extends Canvas
{
	Kernel k;
	public Image m_image;
	public Graphics m_g;
    Dimension m_dimImage; // size of offscreen image

	public Draw_canvas(Kernel k) 
	{
		this.k=k;
		setBackground(Color.black);
	}
	
	public void paint()
	{
		Graphics g = getGraphics();
		paint(g);
	}
	
	public void update(Graphics g)
	{
		paint(g);
	}
	
	public void paint(Graphics g)
	{
		k.ev_can_paint();
		g.drawImage(m_image, 0, 0, null);
	}
	
	public boolean handleEvent(Event ev) 
	{
        if (ev.id == Event.MOUSE_DOWN) 
		{
            k.ev_can_click(ev,this);
			return true;
        }
        return false;
    }
	
    // ResizeImage - resize the off-screen image (if necessary)
    public void ResizeImage()
    {
        // get the size of the applet's window
        Dimension dim = size();
        int nWidth = dim.width;
        int nHeight= dim.height;
		
        // compare that to the size of our image;
        // if it hasn't changed...
        if (m_dimImage != null &&
            m_dimImage.width == nWidth &&
            m_dimImage.height== nHeight)
        {
            // ...don't do anything
            return;
        }
        // create a graphics image to paint to
        m_dimImage = new Dimension(nWidth, nHeight);
        m_image = createImage(nWidth, nHeight);
        m_g = m_image.getGraphics();
    }	
}

class Control_panel extends Panel
{
	Kernel k;
	Button reset_b, mark_b, draw_and_mark_b, del_b;
	Button x_less, x_more, y_less, y_more, z_less, z_more;
	Button lat_less, lat_more, lat_speed_less, lat_speed_more;
	Button lon_less, lon_more, lon_speed_less, lon_speed_more;
	Button dis_less, dis_more, dis_proj_less, dis_proj_more;
	Button eyes_less, eyes_more, sca_less, sca_more;
	TextField x_edit, y_edit, z_edit;
	TextField lat_edit, lon_edit, dis_edit, dis_proj_edit, eyes_edit, sca_edit;
	Label lat_speed, lon_speed;
	Choice cross_eye, proj_type;

	public Control_panel(Kernel k) 
	{
		this.k = k;
		
		GridBagLayout  gridbag  =  new  GridBagLayout();
		GridBagConstraints  c  =  new  GridBagConstraints();
		
		setLayout(gridbag);
		c.fill  =  GridBagConstraints.BOTH; //HORIZONTAL;
		c.weighty  =  1.0;
		
		c.gridy = 0; c.gridx = 0; c.gridwidth = 1;  
		gridbag.setConstraints(del_b = new  Button("Delete Last"),  c); add(del_b);

		c.gridy = 0; c.gridx = 1; c.gridwidth = GridBagConstraints.REMAINDER;
		gridbag.setConstraints(reset_b = new  Button("Reset"),c);add(reset_b);
		
		c.gridy = 1; c.gridx = 0; c.gridwidth = 1;
		gridbag.setConstraints(mark_b = new  Button("Mark"),  c); add(mark_b);
		
		c.gridy = 1; c.gridx = 1; c.gridwidth = GridBagConstraints.REMAINDER;
		gridbag.setConstraints(draw_and_mark_b = new  Button("Draw&Mark"),  c); add(draw_and_mark_b);

		c.gridy = 2; c.gridwidth = 1;
		c.gridx = 0; makelabel(this,"X", gridbag, c);
		c.gridx = 1; gridbag.setConstraints(x_less = new  Button("<"),  c);add(x_less);
		c.gridx = 2; gridbag.setConstraints(x_edit = new TextField("0"), c);add(x_edit);
		c.gridx = 3; gridbag.setConstraints(x_more = new  Button(">"),  c);add(x_more);
		
		c.gridy = 3;
		c.gridx = 0; makelabel(this,"Y", gridbag, c);
		c.gridx = 1; gridbag.setConstraints(y_less = new  Button("<"),  c);add(y_less);
		c.gridx = 2; gridbag.setConstraints(y_edit = new TextField("0"), c);add(y_edit);
		c.gridx = 3; gridbag.setConstraints(y_more = new  Button(">"),  c);add(y_more);
		
		c.gridy = 4;
		c.gridx = 0; makelabel(this,"Z", gridbag, c);
		c.gridx = 1; gridbag.setConstraints(z_less = new  Button("<"),  c);add(z_less);
		c.gridx = 2; gridbag.setConstraints(z_edit = new TextField("0"), c);add(z_edit);
		c.gridx = 3; gridbag.setConstraints(z_more = new  Button(">"),  c);add(z_more);
		
		c.gridy = 5;
		c.gridx = 1; c.gridwidth = 3; makelabel(this,"View Point:", gridbag,  c);
		
		c.gridy = 6;
		c.gridx = 0; c.gridwidth = 1; makelabel(this,"Latitude", gridbag, c);
		c.gridx = 1; gridbag.setConstraints(lat_less = new  Button("<"),  c);add(lat_less);
		c.gridx = 2; gridbag.setConstraints(lat_edit = new TextField("0"), c);add(lat_edit);
		c.gridx = 3; gridbag.setConstraints(lat_more = new  Button(">"),  c);add(lat_more);
		
		c.gridy = 7;
		c.gridx = 0; makelabel(this,"Latitude Speed", gridbag, c);
		c.gridx = 1; gridbag.setConstraints(lat_speed_less = new  Button("<"),  c);add(lat_speed_less);
		c.gridx = 2; lat_speed = makelabel(this,"0", gridbag, c);
		c.gridx = 3; gridbag.setConstraints(lat_speed_more = new  Button(">"),  c);add(lat_speed_more);
		
		c.gridy = 8;
		c.gridx = 0; makelabel(this,"Longitude", gridbag, c);
		c.gridx = 1;gridbag.setConstraints(lon_less = new  Button("<"),  c);add(lon_less);
		c.gridx = 2;gridbag.setConstraints(lon_edit = new TextField("0"), c);add(lon_edit);
		c.gridx = 3;gridbag.setConstraints(lon_more = new  Button(">"),  c);add(lon_more);
		
		c.gridy = 9;
		c.gridx = 0; makelabel(this,"Longit. Speed", gridbag, c);
		c.gridx = 1; gridbag.setConstraints(lon_speed_less = new  Button("<"),  c);add(lon_speed_less);
		c.gridx = 2; lon_speed = makelabel(this,"0", gridbag, c);
		c.gridx = 3; gridbag.setConstraints(lon_speed_more = new  Button(">"),  c);add(lon_speed_more);
		
		c.gridy = 10;
		c.gridx = 0; makelabel(this,"Distance", gridbag, c);
		c.gridx = 1; gridbag.setConstraints(dis_less = new  Button("<"),  c);add(dis_less);
		c.gridx = 2; gridbag.setConstraints(dis_edit = new TextField("10"), c);add(dis_edit);
		c.gridx = 3; gridbag.setConstraints(dis_more = new  Button(">"),  c);add(dis_more);
		
		c.gridy = 11;
		c.gridx = 0; makelabel(this,"Projection Dist.", gridbag, c);
		c.gridx = 1; gridbag.setConstraints(dis_proj_less = new  Button("<"),  c);add(dis_proj_less);
		c.gridx = 2; gridbag.setConstraints(dis_proj_edit = new TextField("1"), c);add(dis_proj_edit);
		c.gridx = 3; gridbag.setConstraints(dis_proj_more = new  Button(">"),  c);add(dis_proj_more);
		

		c.gridy = 12;
		c.gridx = 0;makelabel(this,"Eyes Distance", gridbag, c);
		c.gridx = 1;gridbag.setConstraints(eyes_less = new	Button("<"),  c);add(eyes_less);
		c.gridx = 2;gridbag.setConstraints(eyes_edit = new TextField("1"), c);add(eyes_edit);
		c.gridx = 3;gridbag.setConstraints(eyes_more = new	Button(">"),  c);add(eyes_more);
		
		c.gridy = 13;
		c.gridx = 0;makelabel(this,"Scale", gridbag, c);
		c.gridx = 1;gridbag.setConstraints(sca_less = new  Button("<"), c);add(sca_less);
		c.gridx = 2;gridbag.setConstraints(sca_edit = new TextField("50"), c);add(sca_edit);
		c.gridx = 3;gridbag.setConstraints(sca_more = new  Button(">"), c);add(sca_more);
		
		c.gridy = 14;
		c.gridx = 0; makelabel(this,"Stereo View:", gridbag, c);
		
		c.gridx = 1; c.gridwidth = GridBagConstraints.REMAINDER;
		makelabel(this,"Projec. Type:", gridbag, c);
		
		c.gridy = 15;
		c.gridx = 0; c.gridwidth = 1;
		cross_eye = new Choice();
		cross_eye.addItem("Parallel");
		cross_eye.addItem("Cross Eyes");
		gridbag.setConstraints(cross_eye,  c);               
		add(cross_eye);
		
		c.gridx = 1; c.gridwidth = GridBagConstraints.REMAINDER;
		proj_type = new Choice();
		proj_type.addItem("Eye center");
		proj_type.addItem("Parallel");
		gridbag.setConstraints(proj_type,  c);               
		add(proj_type);
			
		dis_proj_less.disable();
		dis_proj_edit.disable();
		dis_proj_more.disable();

		reset();
	}
	
	public void refrech()
	{
		x_edit.setText(Integer.toString(k.edit_p.x));
		y_edit.setText(Integer.toString(k.edit_p.y));
		z_edit.setText(Integer.toString(k.edit_p.z));
		lat_edit.setText(Integer.toString(k.lat));
		lat_speed.setText(Integer.toString(k.lat_speed));
		lon_edit.setText(Integer.toString(k.lon));
		lon_speed.setText(Integer.toString(k.lon_speed));
		dis_edit.setText(Float.toString(k.dis));
		dis_proj_edit.setText(Float.toString(k.dis_proj));
		eyes_edit.setText(Float.toString(k.eyes));
		sca_edit.setText(Float.toString(k.sca));
	}
	
	public void anim_refrech()
	{
		lat_edit.setText(Integer.toString(k.lat));
	//	lat_speed.setText(Integer.toString(k.lat_speed));
		lon_edit.setText(Integer.toString(k.lon));
	//	lon_speed.setText(Integer.toString(k.lon_speed));
	}
	
	public void reset()
	{
		del_b.disable();
		draw_and_mark_b.disable();
	}

	protected  Label makelabel(Panel pan, String  name, GridBagLayout  gridbag, GridBagConstraints  c)  
	{
		Label label = new Label(name, Label.CENTER);
		gridbag.setConstraints(label,  c);               
		pan.add(label);
		return label;
	}       
	
	public boolean action(Event ev, Object arg) 
	{
        if (ev.target instanceof Button) {
			Button but = (Button)ev.target;
			String sbut = but.getLabel();
			
			if (sbut.compareTo("Mark") == 0)
			{
				draw_and_mark_b.enable();
				k.ev_mark();
				return true;
			}
			if (sbut.compareTo("Draw&Mark") == 0)
			{				
				k.ev_draw_seg();k.ev_mark();
				del_b.enable();
				return true;
			}
			if (sbut.compareTo("Delete Last") == 0)
			{				
				k.delete_seg(k.del_a,k.del_b);
				del_b.disable();
				return true;
			}
			if (sbut.compareTo("Reset") == 0)
			{				
				reset();
				k.ev_reset();
				return true;
			}
			if (but == x_less) {				
				k.edit_p.x -= 1; k.ev_change_val(true); return true;}
			if (but == x_more) {				
				k.edit_p.x += 1; k.ev_change_val(true); return true;}
			if (but == y_less) {				
				k.edit_p.y -= 1; k.ev_change_val(true); return true;}
			if (but == y_more) {				
				k.edit_p.y += 1; k.ev_change_val(true); return true;}
			if (but == z_less) {				
				k.edit_p.z -= 1; k.ev_change_val(true); return true;}
			if (but == z_more) {				
				k.edit_p.z += 1; k.ev_change_val(true); return true;}
			if (but == lat_less) {				
				k.lat -= 5; k.ev_change_val(true); return true;}
			if (but == lat_more) {				
				k.lat += 5; k.ev_change_val(true); return true;}
			if (but == lat_speed_less) {				
				k.lat_speed -= 1; 
				if ((k.lat_speed == 0) && (k.lon_speed == 0))
					k.ev_stop_move();
				else
					k.ev_move();
				k.ev_change_val(true); 
				return true;}
			if (but == lat_speed_more) {				
				k.lat_speed += 1; 
				if ((k.lat_speed == 0) && (k.lon_speed == 0))
					k.ev_stop_move();
				else
					k.ev_move();
				k.ev_change_val(true); 
				return true;}
			if (but == lon_less) {				
				k.lon -= 5; k.ev_change_val(true); return true;}
			if (but == lon_more) {				
				k.lon += 5; k.ev_change_val(true); return true;}
			if (but == lon_speed_less) {				
				k.lon_speed -= 1; 
				if ((k.lat_speed == 0) && (k.lon_speed == 0))
					k.ev_stop_move();
				else
					k.ev_move();
				k.ev_change_val(true); 
				return true;}
			if (but == lon_speed_more) {				
				k.lon_speed += 1; 
				if ((k.lat_speed == 0) && (k.lon_speed == 0))
					k.ev_stop_move();
				else
					k.ev_move();
				k.ev_change_val(true); 
				return true;}
			if (but == dis_less) {				
				if (k.dis >= 3) k.dis -= 1; 
				k.ev_change_val(true); return true;}
			if (but == dis_more) {				
				k.dis += 1; k.ev_change_val(true); return true;}
			if (but == dis_proj_less) {				
				if (k.dis_proj >= 0.2) k.dis_proj -= 0.1; 
				k.ev_change_val(true); return true;}
			if (but == dis_proj_more) {				
				k.dis_proj += 0.1; k.ev_change_val(true); return true;}
			if (but == eyes_less) {				
				if (k.eyes >= 0.11) k.eyes -= 0.1; 
				k.ev_change_val(true); return true;}
			if (but == eyes_more) {				
				k.eyes += 0.1; k.ev_change_val(true); return true;}
			if (but == sca_less) {				
				if (k.sca >= 11) k.sca -= 10; 
				k.ev_change_val(true); return true;}
			if (but == sca_more) {				
				k.sca += 10; k.ev_change_val(true); return true;}
		}
		else {
			if (ev.target == x_edit)
			{				
				k.edit_p.x = Integer.parseInt(x_edit.getText());
				k.ev_change_val(true);
				return true;
			}
			if (ev.target == y_edit)
			{				
				k.edit_p.y = Integer.parseInt(y_edit.getText());
				k.ev_change_val(true);
				return true;
			}
			if (ev.target == z_edit)
			{				
				k.edit_p.z = Integer.parseInt(z_edit.getText());
				k.ev_change_val(true);
				return true;
			}
			if (ev.target == lat_edit)
			{				
				k.lat = Integer.parseInt(lat_edit.getText());
				k.ev_change_val(true);
				return true;
			}
			if (ev.target == lon_edit)
			{				
				k.lon = Integer.parseInt(lon_edit.getText());
				k.ev_change_val(true);
				return true;
			}
			if (ev.target == dis_edit)
			{				
				k.dis = Integer.parseInt(dis_edit.getText());
				k.ev_change_val(true);
				return true;
			}
			if (ev.target == dis_proj_edit)
			{				
				k.dis_proj = Integer.parseInt(dis_proj_edit.getText());
				k.ev_change_val(true);
				return true;
			}
			if (ev.target == eyes_edit)
			{				
				k.eyes = Integer.parseInt(eyes_edit.getText());
				k.ev_change_val(true);
				return true;
			}
			if (ev.target == sca_edit)
			{				
				k.sca = Integer.parseInt(sca_edit.getText());
				k.ev_change_val(true);
				return true;
			}
			if (ev.target == cross_eye)
			{				
				if (cross_eye.getSelectedIndex() == 1)
					k.cross_eyes = true;
				else
					k.cross_eyes = false;
				k.ev_change_val(true);
				return true;
			}
			if (ev.target == proj_type)
			{				
				if (proj_type.getSelectedIndex() == 1) {
					dis_proj_less.disable();
					dis_proj_edit.disable();
					dis_proj_more.disable();
					if (k.pt) k.sca /= 10;
					k.pt = false;
				}
				else {
					dis_proj_less.enable();
					dis_proj_edit.enable();
					dis_proj_more.enable();
					if (!k.pt) k.sca *= 10; 
					k.pt = true;
				}
				k.ev_change_val(true);
				return true;
			}
		}				
		return false;
}
}


class List_point
{
	int point_index;
	List_point next;
	
	public List_point(int p)
	{
		point_index = p;
		next = null;
	}
}

class Point
{
	int x, y, z;
	
	Point next;
	List_point l_p;
	
	static public float x_min = 10000;
	static public float x_max = -10000;
	static public float y_min = 10000;
	static public float y_max = -10000; 
	static public float z_min = 10000;
	static public float z_max = -10000; 
	
	public Point(int xx, int yy, int zz)
	{
		x = xx; y = yy; z = zz;
		next = this;
		l_p = null;
		check_bound();
	}
	
	public Point(Point p)
	{
		this(p.x,p.y,p.z);
		//		check_bound();
	}
	
	public void check_bound()
	{
		if (x<x_min) x_min=x;
		if (y<y_min) y_min=y;
		if (z<z_min) z_min=z;
		if (x>x_max) x_max=x;
		if (y>y_max) y_max=y;
		if (z>z_max) z_max=z;
	}
	
	public void add_list_point(int pp)
	{
		List_point new_point = new List_point(pp);
		if (l_p == null)
			l_p = new_point;
		else
		{
			new_point.next = l_p;
			l_p = new_point;
		}
	}
	
	public boolean remove_list_point(int pp)
	{
		List_point tmp_last = l_p;
		List_point tmp = l_p;
		
		if (tmp != null) {
			while ((tmp.next != null) && (tmp.point_index != pp)) {
				tmp_last = tmp;
				tmp = tmp.next;
			}
			if (tmp.point_index == pp) {
				if (tmp == l_p)
					l_p = tmp.next;
				else
					tmp_last.next = tmp.next;
				return true;
			}
		}
		return false;
	}
	
	public void copy(Point a)
	{
		x = a.x; y = a.y; z = a.z;
	}
	
	public boolean equal(Point a)
	{
		if ((x == a.x) && (y == a.y) && (z == a.z))
			return true;
		else
			return false;
	}
}

class fPoint
{
	float x, y, z;
	
	public fPoint(float xx, float yy, float zz)
	{
		x = xx; y = yy; z = zz;
	}
}

class Vecteur
{
	float dx,dy,dz;
	
	public Vecteur(float xx, float yy, float zz)
	{
		dx = xx; dy = yy; dz = zz;
	}
	
	public Vecteur(Point a, Point b)
	{
		dx = b.x - a.x; dy = b.y - a.y; dz = b.z - a.z;
	}
	
	public Vecteur(fPoint a, Point b)
	{
		dx = b.x - a.x; dy = b.y - a.y; dz = b.z - a.z;
	}
	
	public Vecteur(fPoint a, fPoint b)
	{
		dx = b.x - a.x; dy = b.y - a.y; dz = b.z - a.z;
	}
	
	public void set(fPoint a, fPoint b)
	{
		dx = b.x - a.x; dy = b.y - a.y; dz = b.z - a.z;
	}
	
	public void set(fPoint a, Point b)
	{
		dx = b.x - a.x; dy = b.y - a.y; dz = b.z - a.z;
	}
	
	public void set(float x, float y, float z)
	{
		dx = x; dy = y; dz = z;
	}
	
	public void normalize(float x, float y, float z)
	{
		float tmp = dx*dx + dy*dy + dz*dz;
		if (tmp != 0)
		{
			tmp = (float)Math.sqrt(tmp);
			dx = dx/tmp; dy = dy/tmp; dz = dz/tmp; 
		}
		else
		{
			dx = x; dy = y; dz = z;
			normalize(1,0,0);
		}
	}
	
	public void cross(Vecteur a, Vecteur b)
	{
		dx = a.dy*b.dz-a.dz*b.dy;
		dy = a.dz*b.dx-a.dx*b.dz;
		dz = a.dx*b.dy-a.dy*b.dx;
	}
	
	public float scal(Vecteur a)
	{
		return dx*a.dx+dy*a.dy+dz*a.dz;
	}
}

class Animation extends Thread
{
	Kernel k;

	public Animation(Kernel kk) {
		super();
		k = kk;
	}

	public void run() {
		while (true) {
			k.lon += k.lon_speed;
			k.lat += k.lat_speed;
			k.ev_change_val(false);
		}
	}
}
