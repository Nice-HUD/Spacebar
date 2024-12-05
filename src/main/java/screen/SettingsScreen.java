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

    private final String[] resolutions = {"1024x576", "960x540", "800x600","630x720"};
    private int selectedResolutionIndex = 0;
    private final Frame frame; // Frame 객체
    private boolean resolutionChanged = false; // 해상도 변경 플래그
    private final String[] themeColors = {"Green", "Blue", "Red", "Yellow", "Purple"}; // 변경 가능한 테마 색상
    int selectedColorIndex = 0; // 선택된 테마 색상 옵션
    private int selectedOptionIndex = 0; // 현재 선택된 옵션 (0: 해상도, 1: 소리 조절, 2: 테마 색상)
    private Cooldown selectionCooldown;
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
        this.selectionCooldown = Core.getCooldown(200);
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
                    float volumeValue = (volume - 1) * 6 - 60.0f; // -60 ~ 0 사이의 값을 계산
                    SoundManager.getInstance().modifyAllVolume(volumeValue);
                    this.selectionCooldown.reset();
                    SoundManager.getInstance().playES("menuSelect_es");
                } else if (inputManager.isKeyDown(KeyEvent.VK_RIGHT)) {
                    // 볼륨 증가
                    volume = Math.min(volume + 1, 10);
                    float volumeValue = (volume - 1) * 6 - 60.0f; // -60 ~ 0 사이의 값을 계산
                    SoundManager.getInstance().modifyAllVolume(volumeValue);
                    this.selectionCooldown.reset();
                    SoundManager.getInstance().playES("menuSelect_es");
                }
            }

            if (settingCode == 2) {
                if (inputManager.isKeyDown(KeyEvent.VK_LEFT)) {
                    selectedColorIndex = (selectedColorIndex - 1 + themeColors.length) % themeColors.length;
                    applyThemeColor();
                    SoundManager.getInstance().playES("menuSelect_es");
                    this.inputDelay.reset();
                } else if (inputManager.isKeyDown(KeyEvent.VK_RIGHT)) {
                    selectedColorIndex = (selectedColorIndex + 1) % themeColors.length;
                    applyThemeColor();
                    SoundManager.getInstance().playES("menuSelect_es");
                    this.inputDelay.reset();
                } else if (inputManager.isKeyDown(KeyEvent.VK_ENTER)) {
                    SoundManager.getInstance().playES("menuApply_es");
                    applyThemeColor();
                    this.inputDelay.reset();
                }

            }
        }
    }

    /**
     * Shifts the focus to the next setting menu.
     */
    void nextSettingMenu() {
        settingCode = (settingCode + 1) % 3; // 0, 1, 2 순환s
    }

    /**
     * Shifts the focus to the previous setting menu.
     */
    void previousSettingMenu() {
        settingCode = (settingCode - 1 + 3) % 3; // 2 -> 1 -> 0 순환    }
    }

    /**
     * Draws the elements associated with the screen.
     */
    private void draw() {
        drawManager.initDrawing(this);

        drawManager.drawSettingsMenu(this,
                        resolutions, selectedResolutionIndex, themeColors, selectedColorIndex,
                        selectedOptionIndex, settingCode, volume);

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
    

    /**
     * Current theme color applied to the game UI.
     */
    private Color currentThemeColor = Color.GREEN;

    /**
     * Applies the selected theme color to the game.
     */
    void applyThemeColor() {
        switch (themeColors[selectedColorIndex]) {
            case "Green":
                currentThemeColor = Color.GREEN;
                break;
            case "Blue":
                currentThemeColor = Color.BLUE;
                break;
            case "Red":
                currentThemeColor = Color.RED;
                break;
            case "Yellow":
                currentThemeColor = Color.YELLOW;
                break;
            case "Purple":
                currentThemeColor = new Color(128, 0, 128); // Purple
                break;
            default:
                currentThemeColor = Color.GREEN;
        }

        // Pass the selected theme color to the DrawManager
//        DrawManager.getInstance().setThemeColor(currentThemeColor);
        drawManager.setThemeColor(currentThemeColor);
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

    public void setSelectionCooldown(Cooldown selectionCooldownMock) { this.selectionCooldown = selectionCooldownMock; }

    public int getSelectedColorIndex() { return this.selectedColorIndex; }

    public void setSettingCode(int i) { this.settingCode = i; }

    public int getSettingCode() { return this.settingCode; }


    public int getVolume() { return this.volume; }
}