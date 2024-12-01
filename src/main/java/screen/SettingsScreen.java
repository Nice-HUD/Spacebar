package screen;

import engine.*;
import engine.Frame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;

/**
 * Implements the settings screen.
 */
public class SettingsScreen extends Screen {

    /** Milliseconds between changes in user selection. */
    private static final int SELECTION_TIME = 200;
    /** Time between changes in user selection. */
    private Cooldown selectionCooldown;

    private final String[] resolutions = {"1024x576", "960x540", "800x600","630x720"};
    private int selectedResolutionIndex = 0;
    private final Frame frame; // Frame 객체
    private boolean resolutionChanged = false; // 해상도 변경 플래그

    private int settingCode;
    private int volume;


    /**
     * Constructor, establishes the properties of the screen.
     *
     * @param width  Screen width.
     * @param height Screen height.
     * @param fps    Frames per second, frame rate at which the game is run.
     */
    public SettingsScreen(final int width, final int height, final int fps, final Frame frame) {
        super(width, height, fps);
        this.frame = frame;
        this.isRunning = true; // 초기 상태 활성화
        this.returnCode = 1; // Return to main menu by default
        this.settingCode = 0;
        this.selectionCooldown = Core.getCooldown(SELECTION_TIME);
        this.selectionCooldown.reset();
        this.volume = (int) ((SoundManager.getInstance().getCurrentVolume() + 60) / 6) + 1;
    }

    /**
     * Starts the action.
     *
     * @return Next screen code.
     */
    public final int run() {
        super.run();

        return this.returnCode; // Return code determines next screen
    }

    /**
     * Updates the elements on screen and checks for events.
     */
    protected final void update() {
        super.update();


        // 새 해상도에 맞춰 DrawManager 초기화
        if (resolutionChanged) {
            DrawManager.getInstance().initDrawing(this);
            resolutionChanged = false; // 초기화 완료 후 플래그 해제
        }


        draw();

        if (this.selectionCooldown.checkFinished()&&this.inputDelay.checkFinished()) {
            if (inputManager.isKeyDown(KeyEvent.VK_ESCAPE)) {
                // 메인 메뉴로 돌아가기
                this.isRunning = false;
            }
            if ((inputManager.isKeyDown(KeyEvent.VK_UP)
                    || inputManager.isKeyDown(KeyEvent.VK_W))) {
                previousSettingMenu();
                this.selectionCooldown.reset();
                SoundManager.getInstance().playES("menuSelect_es");
            }
            if (inputManager.isKeyDown(KeyEvent.VK_DOWN)
                    || inputManager.isKeyDown(KeyEvent.VK_S)) {
                nextSettingMenu();
                this.selectionCooldown.reset();
                SoundManager.getInstance().playES("menuSelect_es");
            }

            if(settingCode == 0){
                if (inputManager.isKeyDown(KeyEvent.VK_LEFT)) {
                    // 이전 해상도 선택
                    selectedResolutionIndex = (selectedResolutionIndex - 1 + resolutions.length) % resolutions.length;
                    this.selectionCooldown.reset();
                    SoundManager.getInstance().playES("menuSelect_es");
                } else if (inputManager.isKeyDown(KeyEvent.VK_RIGHT)) {
                    // 다음 해상도 선택
                    selectedResolutionIndex = (selectedResolutionIndex + 1) % resolutions.length;
                    this.selectionCooldown.reset();
                    SoundManager.getInstance().playES("menuSelect_es");
                } else if (inputManager.isKeyDown(KeyEvent.VK_ENTER)) {
                    // 설정 적용
                    applyResolution();
                    resolutionChanged = true; // 해상도 변경 플래그 설정
                    this.inputDelay.reset();
                }
            }

            if (settingCode == 1) {
                if (inputManager.isKeyDown(KeyEvent.VK_LEFT)) {
                    // 볼륨 감소
                    volume = Math.max(volume - 1, 0);
                    float volumeValue = (volume - 1) * 6 - 60; // -60 ~ 0 사이의 값을 계산
                    SoundManager.getInstance().modifyAllVolume(volumeValue);
                    this.selectionCooldown.reset();
                    SoundManager.getInstance().playES("menuSelect_es");
                } else if (inputManager.isKeyDown(KeyEvent.VK_RIGHT)) {
                    // 볼륨 증가
                    volume = Math.min(volume + 1, 10);
                    float volumeValue = (volume - 1) * 6 - 60; // -60 ~ 0 사이의 값을 계산
                    SoundManager.getInstance().modifyAllVolume(volumeValue);
                    this.selectionCooldown.reset();
                    SoundManager.getInstance().playES("menuSelect_es");
                }
            }
        }
    }

    /**
     * Shifts the focus to the next setting menu.
     */
    void nextSettingMenu() {
        if (this.settingCode == 1) // 소리조절에서 화면 해상도 변경으로 이동
            this.settingCode = 0;
        else
            this.settingCode++; // 다음 항목으로 이동
    }

    /**
     * Shifts the focus to the previous setting menu.
     */
    void previousSettingMenu() {
        if (this.settingCode == 0)
            this.settingCode = 1; // 화면 해상도 변경에서 소리조절로 이동
        else
            this.settingCode--; // 이전 항목으로 이동
    }

    /**
     * Draws the elements associated with the screen.
     */
    private void draw() {
        drawManager.initDrawing(this);

        drawManager.drawSettingsMenu(this, settingCode, resolutions, selectedResolutionIndex, volume);


        drawManager.completeDrawing(this);
    }

    /**
     * 바뀐 해상도를 적용시키는 메서드
     * */
    private void applyResolution() {
        String selectedResolution = resolutions[selectedResolutionIndex];
        String[] dimensions = selectedResolution.split("x");

        int newWidth = Integer.parseInt(dimensions[0].trim());
        int newHeight = Integer.parseInt(dimensions[1].trim());


        Core.setWidth(newWidth);
        Core.setHeight(newHeight);

        frame.updateSize(newWidth, newHeight);


        DrawManager.getInstance().initDrawing(this); // 새로운 해상도에 맞게 초기화

    }

    public void setInputManager(InputManager inputManagerMock) {
        this.inputManager = inputManagerMock;
    }

    public void setDrawManager(DrawManager drawManagerMock) {
        this.drawManager = drawManagerMock;
    }

    public int getReturnCode() {
        return this.returnCode;
    }

    public boolean isRunning() {
        return this.isRunning;
    }

    public void setInputDelay(Cooldown inputDelayMock) { this.inputDelay = inputDelayMock; }

    public int getVolume(){return volume;}

    public int getSettingCode(){return settingCode;}
    public void setSettingCode(int settingCode){this.settingCode = settingCode;}

    public void setSelectionCooldown(Cooldown selectionCooldown) {this.selectionCooldown = selectionCooldown;}
}