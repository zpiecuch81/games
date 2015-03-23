package pl.com.ezap.maze3D;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.graphics.g3d.utils.MeshPartBuilder;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.Bullet;
import com.badlogic.gdx.physics.bullet.collision.btCollisionDispatcher;
import com.badlogic.gdx.physics.bullet.collision.btCollisionShape;
import com.badlogic.gdx.physics.bullet.collision.btDbvtBroadphase;
import com.badlogic.gdx.physics.bullet.collision.btDefaultCollisionConfiguration;
import com.badlogic.gdx.physics.bullet.collision.btHeightfieldTerrainShape;
import com.badlogic.gdx.physics.bullet.collision.btSphereShape;
import com.badlogic.gdx.physics.bullet.dynamics.btDiscreteDynamicsWorld;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody;
import com.badlogic.gdx.physics.bullet.dynamics.btSequentialImpulseConstraintSolver;
import com.badlogic.gdx.physics.bullet.linearmath.btMotionState;

public class MazeEscape3D extends ApplicationAdapter {
	private Environment environment;
	private PerspectiveCamera cam;
	private ModelBatch modelBatch;
	private Model model;
	private ModelInstance instance;
	private CameraInputController camController;

	private TiledMap tileMap;

	static class BallMotionState extends btMotionState {
		Matrix4 transform;
		@Override
		public void getWorldTransform (Matrix4 worldTrans) {
			worldTrans.set(transform);
		}
		@Override
		public void setWorldTransform (Matrix4 worldTrans) {
			transform.set(worldTrans);
		}
	}

	private ModelInstance ball;
	private btRigidBody ballBody;
	private BallMotionState ballMotionState;
	private btDiscreteDynamicsWorld world;

	@Override
	public void create () {

		Bullet.init();
		btDefaultCollisionConfiguration collisionConf = new btDefaultCollisionConfiguration();
		world = new btDiscreteDynamicsWorld( new btCollisionDispatcher(collisionConf),
				new btDbvtBroadphase(),
				new btSequentialImpulseConstraintSolver(),
				collisionConf );
		world.setGravity(new Vector3(0.f,0.f,0f));
		world.stepSimulation(1.f/60.f);
		world.updateAabbs();

		readMap();
		createPath();
		createBall();

		ballMotionState = new BallMotionState();
		ballMotionState.transform = ball.transform;
		btSphereShape ballShape = new btSphereShape(0.75f);
		ballBody = new btRigidBody( 1.0f, ballMotionState, ballShape );
		ballBody.setMotionState(ballMotionState);
		ballBody.setLinearVelocity(new Vector3(0,2f,0));
		world.addRigidBody(ballBody);

		environment = new Environment();
		environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));
		environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f));

		cam = new PerspectiveCamera(90, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		cam.position.set( (Integer)tileMap.getProperties().get("width") * 1.5f, 0, 15f);
		cam.lookAt((Integer)tileMap.getProperties().get("width") * 1.5f, (Integer)tileMap.getProperties().get("height") * 1.5f,0);
		cam.near = 1f;
		cam.far = 100f;
		cam.update();

		modelBatch = new ModelBatch();

		camController = new CameraInputController(cam);
		Gdx.input.setInputProcessor(camController);
	}

	@Override
	public void render () {
//		world.updateVehicles(Gdx.graphics.getDeltaTime());
//		world.updateAabbs();
		world.stepSimulation(Gdx.graphics.getDeltaTime());
		camController.update();
		Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

		modelBatch.begin(cam);
		modelBatch.render(instance, environment);
		modelBatch.render(ball, environment);
		modelBatch.end();

		//ball.transform.translate(0, 0.05f, 0);
		//ball
	}

	@Override
	public void dispose () {
		ballMotionState.dispose();
		modelBatch.dispose();
		model.dispose();
	}

	private void readMap()
	{
		try {
			tileMap = new TmxMapLoader().load("maps/maze" + (100) + ".tmx");
		}
		catch(Exception e) {
			System.out.println("Cannot find file: maps/maze" + (100) + ".tmx");
			Gdx.app.exit();
		}
	}

	private void createPath()
	{
		TiledMapTileLayer layer;
		layer = (TiledMapTileLayer) tileMap.getLayers().get("path");

		ModelBuilder mb = new ModelBuilder();
		mb.begin();
		long attributes = VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal; // add TextureCoordinates if you have a texture
		Material material = new Material(ColorAttribute.createDiffuse(Color.MAROON));

		for(int row = 0; row < layer.getHeight(); row++) {
			for(int col = 0; col < layer.getWidth(); col++) {
				Cell cell = layer.getCell(col, row);
				if(cell == null)
					continue;
				if(cell.getTile() == null)
					continue;

				MeshPartBuilder mpb = mb.part("", GL20.GL_TRIANGLES, attributes, material);
				mpb.box(col*3, row*3, 0, 3, 3, 3);

			}
		}

		model = mb.end();
		instance = new ModelInstance(model);

		btHeightfieldTerrainShape terrain = new btHeightfieldTerrainShape(0, 0, null, attributes, attributes, attributes, 0, false);
		btCollisionShape shape = Bullet.obtainStaticNodeShape(instance.nodes);
		btCollistionObject coll = new btCollisionObject
		world.addCollisionObject(shape);
	}

	private void createBall()
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
				ModelBuilder mb = new ModelBuilder();
				long attributes = VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal;
				Material material = new Material(ColorAttribute.createDiffuse(Color.CYAN));
				ball = new ModelInstance(mb.createSphere(1.5f, 1.5f, 2, 32, 32, material, attributes));
				ball.transform.translate(6, 6, 2.5f);
				return;
			}
		}
	}
}
