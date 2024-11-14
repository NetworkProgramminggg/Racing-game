import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;

public class RacingGame extends JFrame {
    private static final int TRACK_WIDTH = 600; // 트랙 가로 길이 (픽셀)
    private static final int TRACK_HEIGHT = 400; // 트랙 세로 길이 (픽셀)
    private static final int MOVE_DISTANCE = 20; // 이동 거리
    private static final int OBSTACLE_MAX_COUNT = 5; // 최대 장애물 개수
    private static final int OBSTACLE_SIZE = 30; // 장애물 크기
    private static final String[] PLAYER_NAMES = {"Alice", "Bob", "Charlie", "David"}; // 플레이어 이름
    private static final int PLAYER_COUNT = PLAYER_NAMES.length; // 플레이어 수

    private JPanel trackPanel;
    private JLabel[] playerLabels;
    private int[] playerXPositions, playerYPositions;
    private JLabel[] obstacleLabels;
    private Random random;

    public RacingGame() {
        random = new Random(); // 랜덤 객체 초기화

        setTitle("네트워크 레이싱 게임 - 자유로운 이동과 장애물 포함");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // 트랙 패널 생성
        trackPanel = new JPanel();
        trackPanel.setLayout(null);
        trackPanel.setPreferredSize(new Dimension(TRACK_WIDTH, TRACK_HEIGHT));
        trackPanel.setBackground(Color.LIGHT_GRAY);

        // 각 플레이어의 위치와 라벨 초기화
        playerXPositions = new int[PLAYER_COUNT];
        playerYPositions = new int[PLAYER_COUNT];
        playerLabels = new JLabel[PLAYER_COUNT];

        for (int i = 0; i < PLAYER_COUNT; i++) {
            playerLabels[i] = new JLabel(PLAYER_NAMES[i]);
            playerLabels[i].setOpaque(true);
            playerLabels[i].setBackground(getPlayerColor(i));
            playerLabels[i].setForeground(Color.WHITE);
            playerLabels[i].setBounds(50, i * 100 + 30, 100, 30); // 초기 위치 설정
            playerXPositions[i] = 50; // X 좌표
            playerYPositions[i] = i * 100 + 30; // Y 좌표
            trackPanel.add(playerLabels[i]);
        }

        // 장애물 생성 (랜덤으로)
        int obstacleCount = random.nextInt(OBSTACLE_MAX_COUNT) + 1; // 장애물 개수 (1 ~ 최대)
        obstacleLabels = new JLabel[obstacleCount];

        for (int i = 0; i < obstacleCount; i++) {
            obstacleLabels[i] = new JLabel("⚠");
            obstacleLabels[i].setOpaque(true);
            obstacleLabels[i].setBackground(Color.BLACK);
            obstacleLabels[i].setForeground(Color.YELLOW);

            // 랜덤 X, Y 위치 설정 (트랙 내에 위치하도록)
            int obstacleX = random.nextInt(TRACK_WIDTH - OBSTACLE_SIZE); // X 좌표
            int obstacleY = random.nextInt(TRACK_HEIGHT - OBSTACLE_SIZE); // Y 좌표
            obstacleLabels[i].setBounds(obstacleX, obstacleY, OBSTACLE_SIZE, OBSTACLE_SIZE);
            trackPanel.add(obstacleLabels[i]);
        }

        add(trackPanel, BorderLayout.CENTER);

        // 키 이벤트 처리 (플레이어 이동)
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                int keyCode = e.getKeyCode();
                if (keyCode == KeyEvent.VK_UP) {
                    movePlayer(0, 0, -MOVE_DISTANCE); // 위로 이동
                } else if (keyCode == KeyEvent.VK_DOWN) {
                    movePlayer(0, 0, MOVE_DISTANCE); // 아래로 이동
                } else if (keyCode == KeyEvent.VK_LEFT) {
                    movePlayer(0, -MOVE_DISTANCE, 0); // 왼쪽으로 이동
                } else if (keyCode == KeyEvent.VK_RIGHT) {
                    movePlayer(0, MOVE_DISTANCE, 0); // 오른쪽으로 이동
                }
            }
        });

        setFocusable(true); // 키 이벤트를 받기 위해 포커스 활성화
    }

    // 플레이어 이동
    private void movePlayer(int playerIndex, int moveX, int moveY) {
        playerXPositions[playerIndex] += moveX;
        playerYPositions[playerIndex] += moveY;

        // 맵 밖으로 나가지 않도록 제한
        playerXPositions[playerIndex] = Math.max(0, Math.min(playerXPositions[playerIndex], TRACK_WIDTH - 100));
        playerYPositions[playerIndex] = Math.max(0, Math.min(playerYPositions[playerIndex], TRACK_HEIGHT - 30));

        if (checkCollisionWithObstacles(playerIndex)) {
            playerXPositions[playerIndex] -= moveX; // 장애물에 부딪히면 이동 취소
            playerYPositions[playerIndex] -= moveY;
            JOptionPane.showMessageDialog(this, PLAYER_NAMES[playerIndex] + "는 장애물에 부딪혔습니다! 후퇴합니다.");
        }

        playerLabels[playerIndex].setLocation(playerXPositions[playerIndex], playerYPositions[playerIndex]);
    }

    // 장애물 충돌 확인
    private boolean checkCollisionWithObstacles(int playerIndex) {
        for (JLabel obstacle : obstacleLabels) {
            Rectangle playerBounds = playerLabels[playerIndex].getBounds();
            Rectangle obstacleBounds = obstacle.getBounds();
            if (playerBounds.intersects(obstacleBounds)) {
                return true; // 충돌 발생
            }
        }
        return false; // 충돌 없음
    }

    // 각 플레이어를 위한 색상 지정
    private Color getPlayerColor(int playerIndex) {
        switch (playerIndex) {
            case 0: return Color.RED;
            case 1: return Color.BLUE;
            case 2: return Color.GREEN;
            case 3: return Color.ORANGE;
            default: return Color.BLACK;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            RacingGame clientUI = new RacingGame();
            clientUI.setVisible(true);
        });
    }
}
