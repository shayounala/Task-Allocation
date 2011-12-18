package source;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.layout.FillLayout;

public class DisplayUI {

	/**
	 * Launch the application.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			DisplayUI window = new DisplayUI();
			System.out.println("Start UI Initiation");
			window.open();
			System.out.println("Finish UI Initiation");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private static Shell shell;

	public static Shell getShell() {
		return shell;
	}

	private static Shell cooperationshell;
	private static Shell transfershell;

	private Shell getshells(int type) {
		switch (type) {
		case 0:
			return shell;
		case 1:
			return cooperationshell;
		case 2:
			return transfershell;
		default:
			return null;
		}
	}

	private static Canvas canvas;

	public static Canvas getCanvas() {
		return canvas;
	}

	private static Canvas cooperationcanvas;

	public static Canvas getCooperationcanvas() {
		return cooperationcanvas;
	}

	public static Canvas getTransfercanvas() {
		return transfercanvas;
	}

	private static Canvas transfercanvas;

	private Canvas getcanvass(int type) {
		switch (type) {
		case 0:
			return canvas;
		case 1:
			return cooperationcanvas;
		case 2:
			return transfercanvas;
		default:
			return null;
		}
	}

	private static Composite composite;
	private static Composite cooperationcomposite;
	private static Composite transfercomposite;

	private Composite getcomposites(int type) {
		switch (type) {
		case 0:
			return composite;
		case 1:
			return cooperationcomposite;
		case 2:
			return transfercomposite;
		default:
			return null;
		}
	}

	/**
	 * Open the window.
	 */
	public void open() {
		Display display = Display.getDefault();

		shell = new Shell();
		cooperationshell = new Shell();
		transfershell = new Shell();

		composite = new Composite(shell, SWT.NONE);
		cooperationcomposite = new Composite(cooperationshell, SWT.NONE);
		transfercomposite = new Composite(transfershell, SWT.NONE);

		canvas = new Canvas(composite, SWT.DOUBLE_BUFFERED);
		cooperationcanvas = new Canvas(cooperationcomposite,
				SWT.DOUBLE_BUFFERED);
		transfercanvas = new Canvas(transfercomposite, SWT.DOUBLE_BUFFERED);

		InitialShell(0);
		InitialShell(1);
		InitialShell(2);

		Experiment.main(null);

		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	private void InitialShell(int type) {
		// TODO Auto-generated method stub

		this.getshells(type).setSize(800, 600);
		this.getshells(type).setText("SWT Application");
		GridLayout gl_shell = new GridLayout(1, false);
		gl_shell.marginHeight = 0;
		gl_shell.verticalSpacing = 0;
		gl_shell.marginWidth = 0;
		this.getshells(type).setLayout(gl_shell);

		/*
		 * ToolBar toolBar = new ToolBar(newshell, SWT.FLAT | SWT.RIGHT);
		 * toolBar.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 1,
		 * 1));
		 * 
		 * ToolItem tltmStart = new ToolItem(toolBar, SWT.NONE);
		 * tltmStart.addSelectionListener(new SelectionAdapter() {
		 * 
		 * @Override public void widgetSelected(SelectionEvent e) { } });
		 * tltmStart.setText("Start");
		 * 
		 * ToolItem tltmPause = new ToolItem(toolBar, SWT.NONE);
		 * tltmPause.setText("Pause");
		 * 
		 * ToolItem tltmStop = new ToolItem(toolBar, SWT.NONE);
		 * tltmStop.setText("Stop");
		 * 
		 * ToolItem tltmSettings = new ToolItem(toolBar, SWT.NONE);
		 * 
		 * tltmSettings.setText("Settings");
		 */

		GridLayout gl_composite = new GridLayout(1, false);
		gl_composite.verticalSpacing = 0;
		gl_composite.marginWidth = 0;
		gl_composite.marginHeight = 0;
		this.getcomposites(type).setLayout(gl_composite);
		this.getcomposites(type).setLayoutData(
				new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

		System.out.println(this.getcanvass(type) == null);
		System.out.println(canvas == null);
		this.getcanvass(type).setLayoutData(
				new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		this.getcanvass(type).setLayout(new FillLayout(SWT.HORIZONTAL));

		this.getshells(type).open();
		this.getshells(type).layout();
	}

}
