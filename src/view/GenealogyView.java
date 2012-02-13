package com.qualityeclipse.genealogy.view;

import org.eclipse.draw2d.*;
import org.eclipse.draw2d.geometry.*;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.part.ViewPart;

import com.qualityeclipse.genealogy.listener.FigureMover;

/**
 * A example view displaying genealogy using the Draw2D and GEF frameworks
 */
public class GenealogyView extends ViewPart
{
	/**
	 * Add a canvas on which the diagram is rendered and figures representing the various
	 * people and their relationships.
	 * 
	 * @param parent the composite to which the diagram is added
	 */
	private Canvas createDiagram(Composite parent) {

		// Create a root figure and simple layout to contain all other figures
		Figure root = new Figure();
		root.setFont(parent.getFont());
		root.setLayoutManager(new XYLayout());

		// Add the father "Andy"
		IFigure andy = createPersonFigure("Andy");
		root.add(andy, new Rectangle(new Point(10, 10), andy.getPreferredSize()));

		// Add the mother "Betty"
		IFigure betty = createPersonFigure("Betty");
		root.add(betty, new Rectangle(new Point(230, 10), betty.getPreferredSize()));

		// Add the son "Carl"
		IFigure carl = createPersonFigure("Carl");
		root.add(carl, new Rectangle(new Point(120, 120), carl.getPreferredSize()));

		// Add a "marriage"
		IFigure marriage = createMarriageFigure();
		root.add(marriage, new Rectangle(new Point(145, 35), marriage.getPreferredSize()));

		// Add a Star
		IFigure line = createLineFigure();
		root.add(line, new Rectangle (new Point(300,300), line.getPreferredSize()));

		
		// Add lines connecting the figures
		root.add(connect(andy, marriage));
		root.add(connect(betty, marriage));
		root.add(connect(carl, marriage));

		// Create the canvas and LightweightSystem
		// and use it to show the root figure in the shell
		Canvas canvas = new Canvas(parent, SWT.DOUBLE_BUFFERED);
		canvas.setBackground(ColorConstants.white);
		LightweightSystem lws = new LightweightSystem(canvas);
		lws.setContents(root);
		return canvas;
	}

	/**
	 * Create a rectangle figure representing a person
	 * 
	 * @param name the name of the person
	 * @return the figure (not <code>null</code>)
	 */
	private IFigure createPersonFigure(String name) {
		RectangleFigure rectangleFigure = new RectangleFigure();
		rectangleFigure.setBackgroundColor(ColorConstants.lightGray);
		rectangleFigure.setLayoutManager(new ToolbarLayout());
		rectangleFigure.setPreferredSize(100, 100);
		rectangleFigure.add(new Label(name));
		new FigureMover(rectangleFigure);
		return rectangleFigure;
		
		
	}
	
	/**
	 * Create a diamond figure representing a marriage
	 * 
	 * @return the figure (not <code>null</code>)
	 */
	private IFigure createMarriageFigure() {
		Rectangle r = new Rectangle(0, 0, 50, 50);
		PolygonShape polygonShape = new PolygonShape();
		polygonShape.setStart(r.getTop());
		polygonShape.addPoint(r.getTop());
		polygonShape.addPoint(r.getLeft());
		polygonShape.addPoint(r.getBottom());
		polygonShape.addPoint(r.getRight());
		polygonShape.addPoint(r.getTop());
		polygonShape.setEnd(r.getTop());
		polygonShape.setFill(true);
		polygonShape.setBackgroundColor(ColorConstants.lightGray);
		polygonShape.setPreferredSize(r.getSize());
		new FigureMover(polygonShape);
		return polygonShape;
	}
	/**
	*Create a Star
	*
	*/
	private IFigure createLineFigure() {
	Polyline line = new Polyline();
	line.addPoint(new Point(300, 300));
	line.addPoint(new Point(350, 300));
	line.addPoint(new Point(375, 250));
	line.addPoint(new Point(400, 300));
	line.addPoint(new Point(450, 300));
	line.addPoint(new Point(400, 325));
	line.addPoint(new Point(450, 375));
	line.addPoint(new Point(375, 350));
	line.addPoint(new Point(300, 375));
	line.addPoint(new Point(350, 325));
	line.addPoint(new Point(300, 300));
	line.setFill(true);
	return line;

	}

	/**
	 * Create a line connecting two figures
	 * 
	 * @param figure1 a figure (not <code>null</code>)
	 * @param figure2 a figure (not <code>null</code>)
	 * @return the line connecting the figures (not <code>null</code>)
	 */
	private Connection connect(IFigure figure1, IFigure figure2) {
		PolylineConnection connection = new PolylineConnection();
		connection.setSourceAnchor(new ChopboxAnchor(figure1));
		connection.setTargetAnchor(new ChopboxAnchor(figure2));
		return connection;
	}

	//=============================================
	// Diagram

	public void createPartControl(Composite parent) {
		createDiagram(parent);
	}

	public void setFocus() {
	}

	//=============================================
	// Standalone Shell
	
	/**
	 * The main application entry point
	 */
	public static void main(String[] args) {
		new GenealogyView().run();
	}

	/**
	 * Create, initialize, and run the shell.
	 * Call createDiagram to create the Draw2D diagram.
	 */
	private void run() {
		Shell shell = new Shell(new Display());
		shell.setSize(365, 280);
		shell.setText("Genealogy");
		shell.setLayout(new GridLayout());
		
		Canvas canvas = createDiagram(shell);
		canvas.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		Display display = shell.getDisplay();
		shell.open();
		while (!shell.isDisposed()) {
			while (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}
}
