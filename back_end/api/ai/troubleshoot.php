<?php
/**
 * AI Troubleshooting Endpoint
 * POST /api/ai/troubleshoot.php
 * 
 * Body: {
 *   "problem_description": "My car engine won't start"
 * }
 * 
 * This endpoint provides step-by-step troubleshooting solutions.
 * Currently rule-based, but ready for OpenAI integration.
 */

require_once __DIR__ . '/../config/headers.php';
require_once __DIR__ . '/../config/db.php';

// Only allow POST requests
if ($_SERVER['REQUEST_METHOD'] !== 'POST') {
    http_response_code(405);
    echo json_encode(['success' => false, 'message' => 'Method not allowed']);
    exit();
}

// Get JSON input
$input = json_decode(file_get_contents('php://input'), true);

// Validate required fields
if (empty($input['problem_description'])) {
    http_response_code(400);
    echo json_encode(['success' => false, 'message' => 'problem_description is required']);
    exit();
}

$problemDescription = strtolower(trim($input['problem_description']));

/**
 * Rule-based troubleshooting (can be replaced with OpenAI API)
 */
function getTroubleshootingSteps($problem) {
    $solutions = [];
    
    // Engine won't start
    if (preg_match('/engine.*(won\'t|not|doesn\'t).*start|car.*(won\'t|not|doesn\'t).*start|start.*problem/i', $problem)) {
        $solutions = [
            [
                'step' => 1,
                'title' => 'Check Battery',
                'description' => 'Inspect battery terminals for corrosion. Test battery voltage (should be 12.6V or higher). If voltage is low, try jump-starting the vehicle.',
                'priority' => 'high'
            ],
            [
                'step' => 2,
                'title' => 'Check Fuel Level',
                'description' => 'Ensure your vehicle has sufficient fuel. Low fuel can prevent the engine from starting.',
                'priority' => 'medium'
            ],
            [
                'step' => 3,
                'title' => 'Check Ignition System',
                'description' => 'Inspect spark plugs and ignition coils. Look for signs of wear or damage. Replace if necessary.',
                'priority' => 'medium'
            ],
            [
                'step' => 4,
                'title' => 'Check Starter Motor',
                'description' => 'Listen for clicking sounds when turning the key. If you hear clicks but engine doesn\'t turn, the starter motor may be faulty.',
                'priority' => 'high'
            ],
            [
                'step' => 5,
                'title' => 'Check Fuel Pump',
                'description' => 'Turn the key to ON position and listen for a humming sound from the fuel tank. No sound may indicate a faulty fuel pump.',
                'priority' => 'medium'
            ]
        ];
    }
    // Battery issues
    elseif (preg_match('/battery|dead.*battery|battery.*dead|no.*power/i', $problem)) {
        $solutions = [
            [
                'step' => 1,
                'title' => 'Check Battery Connections',
                'description' => 'Ensure battery terminals are clean and tightly connected. Clean any corrosion with baking soda and water.',
                'priority' => 'high'
            ],
            [
                'step' => 2,
                'title' => 'Test Battery Voltage',
                'description' => 'Use a multimeter to check battery voltage. A fully charged battery should read 12.6V or higher.',
                'priority' => 'high'
            ],
            [
                'step' => 3,
                'title' => 'Jump Start the Vehicle',
                'description' => 'If battery is low, use jumper cables to jump-start the vehicle. Let it run for at least 15 minutes to recharge.',
                'priority' => 'medium'
            ],
            [
                'step' => 4,
                'title' => 'Check Alternator',
                'description' => 'If battery keeps dying, the alternator may not be charging properly. Have it tested at an auto parts store.',
                'priority' => 'medium'
            ]
        ];
    }
    // Overheating
    elseif (preg_match('/overheat|temperature|hot|coolant|radiator/i', $problem)) {
        $solutions = [
            [
                'step' => 1,
                'title' => 'Turn Off Engine Immediately',
                'description' => 'If temperature gauge is in the red zone, turn off the engine to prevent serious damage.',
                'priority' => 'critical'
            ],
            [
                'step' => 2,
                'title' => 'Check Coolant Level',
                'description' => 'Wait for engine to cool, then check coolant reservoir. Add coolant if low (use proper coolant type for your vehicle).',
                'priority' => 'high'
            ],
            [
                'step' => 3,
                'title' => 'Check for Leaks',
                'description' => 'Inspect radiator, hoses, and water pump for visible leaks. Look for puddles under the vehicle.',
                'priority' => 'high'
            ],
            [
                'step' => 4,
                'title' => 'Check Radiator Fan',
                'description' => 'Ensure radiator fan is working. If not running, check fuse and fan motor.',
                'priority' => 'medium'
            ],
            [
                'step' => 5,
                'title' => 'Check Thermostat',
                'description' => 'A stuck thermostat can cause overheating. Have it tested and replaced if necessary.',
                'priority' => 'medium'
            ]
        ];
    }
    // Brake issues
    elseif (preg_match('/brake|braking|stop|pedal|squeak|grind/i', $problem)) {
        $solutions = [
            [
                'step' => 1,
                'title' => 'Check Brake Fluid Level',
                'description' => 'Check brake fluid reservoir. Low fluid may indicate a leak in the brake system.',
                'priority' => 'high'
            ],
            [
                'step' => 2,
                'title' => 'Inspect Brake Pads',
                'description' => 'Check brake pad thickness through wheel spokes. Replace if less than 1/4 inch thick.',
                'priority' => 'high'
            ],
            [
                'step' => 3,
                'title' => 'Check for Leaks',
                'description' => 'Inspect brake lines and calipers for fluid leaks. Address immediately if found.',
                'priority' => 'critical'
            ],
            [
                'step' => 4,
                'title' => 'Test Brake Pedal',
                'description' => 'If pedal feels spongy or goes to floor, there may be air in brake lines. Bleed brakes if needed.',
                'priority' => 'high'
            ]
        ];
    }
    // Tire issues
    elseif (preg_match('/tire|flat|puncture|pressure|bald/i', $problem)) {
        $solutions = [
            [
                'step' => 1,
                'title' => 'Check Tire Pressure',
                'description' => 'Use a tire pressure gauge to check all tires. Inflate to manufacturer\'s recommended PSI.',
                'priority' => 'high'
            ],
            [
                'step' => 2,
                'title' => 'Inspect for Punctures',
                'description' => 'Look for nails, screws, or other objects in the tire. Check for slow leaks with soapy water.',
                'priority' => 'high'
            ],
            [
                'step' => 3,
                'title' => 'Check Tread Depth',
                'description' => 'Use a penny test: insert penny head-first into tread. If you can see all of Lincoln\'s head, replace tire.',
                'priority' => 'medium'
            ],
            [
                'step' => 4,
                'title' => 'Use Spare Tire',
                'description' => 'If tire is flat, replace with spare tire. Ensure spare is properly inflated before use.',
                'priority' => 'high'
            ]
        ];
    }
    // Electrical issues
    elseif (preg_match('/electrical|fuse|light|wiring|short|circuit/i', $problem)) {
        $solutions = [
            [
                'step' => 1,
                'title' => 'Check Fuses',
                'description' => 'Inspect fuse box for blown fuses. Replace with same amperage fuse if found.',
                'priority' => 'high'
            ],
            [
                'step' => 2,
                'title' => 'Check Battery Connections',
                'description' => 'Ensure battery terminals are clean and secure. Loose connections can cause electrical issues.',
                'priority' => 'high'
            ],
            [
                'step' => 3,
                'title' => 'Inspect Wiring',
                'description' => 'Look for damaged, frayed, or exposed wires. Do not touch bare wires - seek professional help.',
                'priority' => 'medium'
            ],
            [
                'step' => 4,
                'title' => 'Check Alternator',
                'description' => 'If electrical components are failing, test alternator output. Should be 13.5-14.5V when running.',
                'priority' => 'medium'
            ]
        ];
    }
    // Default generic solution
    else {
        $solutions = [
            [
                'step' => 1,
                'title' => 'Identify the Problem',
                'description' => 'Carefully observe and note all symptoms. Check warning lights on dashboard.',
                'priority' => 'high'
            ],
            [
                'step' => 2,
                'title' => 'Check Basic Components',
                'description' => 'Inspect fluid levels (oil, coolant, brake fluid), battery connections, and tire pressure.',
                'priority' => 'high'
            ],
            [
                'step' => 3,
                'title' => 'Consult Vehicle Manual',
                'description' => 'Refer to your vehicle\'s owner manual for specific troubleshooting steps related to your issue.',
                'priority' => 'medium'
            ],
            [
                'step' => 4,
                'title' => 'Seek Professional Help',
                'description' => 'If problem persists or you\'re unsure, contact a qualified mechanic for assistance.',
                'priority' => 'medium'
            ]
        ];
    }
    
    return $solutions;
}

/**
 * OpenAI Integration (Ready for implementation)
 * Uncomment and configure when ready to use OpenAI API
 */
/*
function getOpenAITroubleshooting($problem) {
    $apiKey = 'YOUR_OPENAI_API_KEY';
    $url = 'https://api.openai.com/v1/chat/completions';
    
    $data = [
        'model' => 'gpt-3.5-turbo',
        'messages' => [
            [
                'role' => 'system',
                'content' => 'You are an expert automotive mechanic. Provide step-by-step troubleshooting solutions for vehicle problems.'
            ],
            [
                'role' => 'user',
                'content' => "Provide detailed troubleshooting steps for this vehicle problem: " . $problem
            ]
        ],
        'max_tokens' => 1000,
        'temperature' => 0.7
    ];
    
    $ch = curl_init($url);
    curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
    curl_setopt($ch, CURLOPT_POST, true);
    curl_setopt($ch, CURLOPT_POSTFIELDS, json_encode($data));
    curl_setopt($ch, CURLOPT_HTTPHEADER, [
        'Content-Type: application/json',
        'Authorization: Bearer ' . $apiKey
    ]);
    
    $response = curl_exec($ch);
    curl_close($ch);
    
    $result = json_decode($response, true);
    
    if (isset($result['choices'][0]['message']['content'])) {
        return $result['choices'][0]['message']['content'];
    }
    
    return null;
}
*/

try {
    // Use rule-based troubleshooting (can be switched to OpenAI)
    $solutions = getTroubleshootingSteps($problemDescription);
    
    // If OpenAI is enabled, uncomment below:
    // $openAISolution = getOpenAITroubleshooting($problemDescription);
    // if ($openAISolution) {
    //     $solutions = parseOpenAIResponse($openAISolution);
    // }
    
    echo json_encode([
        'success' => true,
        'message' => 'Troubleshooting steps generated',
        'data' => [
            'problem_description' => $input['problem_description'],
            'solutions_count' => count($solutions),
            'solutions' => $solutions,
            'note' => 'If no mechanic is available, try these troubleshooting steps. For complex issues, always consult a professional mechanic.'
        ]
    ]);
    
} catch (Exception $e) {
    http_response_code(500);
    echo json_encode([
        'success' => false,
        'message' => 'Failed to generate troubleshooting steps',
        'error' => $e->getMessage()
    ]);
}

