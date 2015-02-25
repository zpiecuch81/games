package pl.com.ezap.mazeescape.states;

import static pl.com.ezap.mazeescape.handlers.B2DVars.PPM;
import pl.com.ezap.mazeescape.entities.HUD;
import pl.com.ezap.mazeescape.entities.Player;
import pl.com.ezap.mazeescape.handlers.B2DVars;
import pl.com.ezap.mazeescape.handlers.BBContactListener;
import pl.com.ezap.mazeescape.handlers.BBInput;
import pl.com.ezap.mazeescape.handlers.BoundedCamera;
import pl.com.ezap.mazeescape.handlers.GameStateManager;
import pl.com.ezap.mazeescape.handlers.MenuButton;
import pl.com.ezap.mazeescape.handlers.Velocity;
import pl.com.ezap.mazeescape.main.Game;
import pl.com.ezap.mazeescape.utils.CountDown;
import pl.com.ezap.mazeescape.utils.Scores;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

public class Play extends GameState {

	private boolean debug = false;

	private World world;
	private Box2DDebugRenderer b2dRenderer;
	private BBContactListener cl;
	private BoundedCamera b2dCam;
	private Velocity velocity;
	private CountDown countDown;

	private Player player;

	private TiledMap tileMap;
	private int tileMapWidth;
	private int tileMapHeight;
	private int tileSize;
	private Vector2 startPosition;
	private OrthogonalTiledMapRenderer tmRenderer;

	private float maxZoom = 1;
	private float baseZoom = 1;
	private float zoomStep = 1;
	private float currentZoom = 1;

	private MenuButton menu;
	private enum Phase { COUNTDOWN, PLAY, ZOOM};
	private Phase phase = Phase.COUNTDOWN;

	private HUD hud;

	public static int level;

	public Play(GameStateManager gsm)
	{
		super(gsm);
		world = new World(new Vector2(0, 0), true);
		cl = new BBContactListener();
		world.setContactListener(cl);
		b2dRenderer = new Box2DDebugRenderer();
		velocity = new Velocity();
		countDown = new CountDown(3);

		readMap();
		setCameraAndZoom();
		setHintVisibility(false);

		createWalls();
		setStartPosition();
		createEndBody();

		createPlayer();

		hud = new HUD(player);

		b2dCam = new BoundedCamera();
		b2dCam.setToOrtho(false, Game.V_WIDTH / PPM, Game.V_HEIGHT / PPM);
		b2dCam.setBounds(0, (tileMapWidth * tileSize) / PPM, 0, (tileMapHeight * tileSize) / PPM);

		createMenu();
	}

	private void readMap()
	{
		try {
			tileMap = new TmxMapLoader().load("maps/maze" + (level+99) + ".tmx");
		}
		catch(Exception e) {
			System.out.println("Cannot find file: maps/maze" + (level+99) + ".tmx");
			Gdx.app.exit();
		}
		tileMapWidth = (Integer) tileMap.getProperties().get("width");
		tileMapHeight = (Integer) tileMap.getProperties().get("height");
		tileSize = (Integer) tileMap.getProperties().get("tilewidth");
		tmRenderer = new OrthogonalTiledMapRenderer(tileMap);

		String bestTime = (String)(tileMap.getProperties().get("bestTimePossible"));
		if(bestTime.isEmpty()) {
			System.out.println("Error: Map: res/maps/maze" + (level+99) + ".tmx has no possible time set!");
		} else {
			Scores scores = new Scores();
			scores.setPossibleTime(level, Float.valueOf(bestTime));
		}
	}

	private void setCameraAndZoom()
	{
		float screenProportions = (float)Game.V_HEIGHT / (float)Game.V_WIDTH;
		float viewPortX = Math.max(6*tileSize, (float)Game.V_WIDTH);
		float viewPortY = Math.max(6*tileSize*screenProportions, (float)Game.V_HEIGHT);
		cam.setToOrtho(false, viewPortX, viewPortY);
		cam.setBounds(0, tileMapWidth * tileSize, 0, tileMapHeight * tileSize);

		baseZoom = (6*tileSize)/viewPortX;
		float maxVerticalZoom = tileMapHeight * tileSize / cam.viewportHeight;
		float maxHorizontalZoom = tileMapWidth * tileSize / cam.viewportWidth;
		maxZoom = maxVerticalZoom > maxHorizontalZoom ? maxVerticalZoom : maxHorizontalZoom;
		if( maxZoom < baseZoom ) {
			maxZoom = baseZoom;
		}
		zoomStep = ( maxZoom - baseZoom ) / 20;
		currentZoom = maxZoom;
	}

	private void createPlayer()
	{
		BodyDef bdef = new BodyDef();
		bdef.type = BodyType.DynamicBody;
		bdef.position.set(startPosition);
		bdef.fixedRotation = true;
		bdef.linearVelocity.set(0f, 0f);

		Body body = world.createBody(bdef);

		CircleShape shape = new CircleShape();
		shape.setRadius(14 / PPM);

		FixtureDef fdef = new FixtureDef();
		fdef.shape = shape;
		fdef.density = 0;
		fdef.friction = 0;
		fdef.restitution = 0;
		fdef.filter.categoryBits = B2DVars.BIT_PLAYER;
		fdef.filter.maskBits = B2DVars.BIT_END | B2DVars.BIT_WALL;

		body.createFixture(fdef).setUserData("player");
		shape.dispose();

		player = new Player(body);
		body.setUserData(player);
	}

	private void createWalls()
	{
		TiledMapTileLayer layer;
		layer = (TiledMapTileLayer) tileMap.getLayers().get("wall");
		createBlocks(layer, B2DVars.BIT_WALL);
	}

	private void createBlocks(TiledMapTileLayer layer, short bits)
	{
		for(int row = 0; row < layer.getHeight(); row++) {
			for(int col = 0; col < layer.getWidth(); col++) {
				Cell cell = layer.getCell(col, row);
				if(cell == null)
					continue;
				if(cell.getTile() == null)
					continue;

				BodyDef bdef = new BodyDef();
				bdef.type = BodyType.StaticBody;
				bdef.position.set((col + 0.5f) * tileSize / PPM, (row + 0.5f) * tileSize / PPM);

				PolygonShape cs = new PolygonShape();
				cs.setAsBox(tileSize/2/PPM, tileSize/2/PPM);
				FixtureDef fd = new FixtureDef();
				fd.friction = 0;
				fd.shape = cs;
				fd.restitution = 0;
				fd.density = 0;
				fd.filter.categoryBits = bits;
				fd.filter.maskBits = B2DVars.BIT_PLAYER;
				world.createBody(bdef).createFixture(fd);
				cs.dispose();
			}
		}
		
	}

	private void setStartPosition()
	{
		TiledMapTileLayer layer;
		layer = (TiledMapTileLayer) tileMap.getLayers().get("start");
		for(int row = 0; row < layer.getHeight(); row++) {
			for(int col = 0; col < layer.getWidth(); col++) {
				Cell cell = layer.getCell(col, row);
				if(cell == null)
					continue;
				if(cell.getTile() == null)
					continue;
				startPosition = new Vector2((col + 0.5f) * tileSize / PPM, (row + 0.5f) * tileSize / PPM );
			}
		}

		if( startPosition == null ) {
			System.out.println("ERROR: no start position!");
			gsm.setState(GameStateManager.MENU);
		}
	}

	private void createEndBody()
	{
		TiledMapTileLayer layer;
		layer = (TiledMapTileLayer) tileMap.getLayers().get("end");
		for(int row = 0; row < layer.getHeight(); row++) {
			for(int col = 0; col < layer.getWidth(); col++) {
				Cell cell = layer.getCell(col, row);
				if(cell == null)
					continue;
				if(cell.getTile() == null)
					continue;
				BodyDef bdef = new BodyDef();
				bdef.type = BodyType.StaticBody;
				PolygonShape polygon = new PolygonShape();
				bdef.position.set( (col + 0.5f) * tileSize / PPM, (row + 0.5f) * tileSize / PPM);
				polygon.setAsBox(tileSize/8/PPM, tileSize/8/PPM);
				Body body = world.createBody(bdef);
				FixtureDef fdef = new FixtureDef();
				fdef.shape = polygon;
				fdef.isSensor = true;
				fdef.filter.categoryBits = B2DVars.BIT_END;
				fdef.filter.maskBits = B2DVars.BIT_PLAYER;
				body.createFixture(fdef).setUserData("end");
				return;
			}
		}
	}

	private void createMenu()
	{
		menu = new MenuButton(
				(int)(Game.V_HEIGHT *0.05),
				(int)(Game.V_HEIGHT * 0.95),
				(int)(Game.V_HEIGHT * 0.05),
				(int)(Game.V_HEIGHT * 0.05),
				hudCam);
		menu.setText("M");
	}

	public void handleInput()
	{
		if(BBInput.isDown(BBInput.ZOOMIN)) {
			currentZoom += zoomStep;
			phase = Phase.ZOOM;
		} else {
			currentZoom -= zoomStep;
			if(BBInput.isPressed(BBInput.UP)) {
				velocity.up();
			}
			if(BBInput.isPressed(BBInput.DOWN)) {
				velocity.down();
			}
			if(BBInput.isPressed(BBInput.LEFT)) {
				velocity.left();
			}
			if(BBInput.isPressed(BBInput.RIGHT)) {
				velocity.right();
			}
		}
		setHintVisibility(false);
		if( currentZoom > maxZoom ){
			currentZoom = maxZoom;
			setHintVisibility(true);
		} else if(currentZoom < baseZoom){
			currentZoom = baseZoom;
			phase = Phase.PLAY;
		}
	}

	public void update(float dt)
	{
		switch( phase ) {
		case COUNTDOWN:
			updateCountDown(dt);
			break;
		case PLAY:
			updatePlay(dt);
			break;
		case ZOOM:
			updateZoom(dt);
			break;
		default:
			break;
		}
	}

	private void updateCountDown( float dt )
	{
		countDown.update(dt);
		if( !countDown.isStillCounting() )
			phase = Phase.PLAY;
	}

	private void updatePlay(float dt)
	{
		player.addTime(dt);

		handleInput();

		velocity.update(dt);
		if( velocity.shouldSwitch() ) {
			player.getBody().setLinearVelocity( velocity.getVelocity() );
		}
		world.step(Game.STEP, 1, 1);

		player.update(dt);
		menu.update(dt);

		if( menu.isClicked() ) {
			gsm.pushState(GameStateManager.IN_GAME_OPTIONS);
		}
		if( cl.isEnd() ) {
			Results.player = player;
			gsm.setState(GameStateManager.RESULT);
		}
		if( cl.wallHit() ) {
			//Game.res.getSound("hit").play(0.5f);
			velocity.none();
		}
	}

	private void updateZoom(float dt)
	{
		player.addTime(dt);
		handleInput();
	}

	public void render()
	{
		cam.setZoom(currentZoom);
		cam.setPosition(player.getPosition().x * PPM, player.getPosition().y * PPM);
		cam.update();

		sb.setProjectionMatrix(hudCam.combined);
		Gdx.gl.glClearColor(0, 0, 0, 0);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		super.render();

		tmRenderer.setView(cam);
		tmRenderer.render();

		sb.setProjectionMatrix(cam.combined);
		player.render(sb);

		sb.setProjectionMatrix(hudCam.combined);
		hud.render(sb);
		countDown.render(sb);

		if(phase == Phase.PLAY) {
			menu.render(sb);
		}

		if(debug) {
			b2dCam.setPosition(player.getPosition().x + Game.V_WIDTH / 4 / PPM, Game.V_HEIGHT / 2 / PPM);
			b2dCam.update();
			b2dRenderer.render(world, b2dCam.combined);
		}
	}

	public void dispose() {
		menu.dispose();
	}

	private void setHintVisibility(boolean visible) {
		TiledMapTileLayer layer;
		layer = (TiledMapTileLayer) tileMap.getLayers().get("hint");
		layer.setVisible(visible);
		layer.setOpacity(0.5f);
	}

	@Override
	public void back() {
		gsm.setState(GameStateManager.MENU);
	}
}